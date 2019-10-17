package com.eservice.iot.web;

import com.alibaba.fastjson.JSON;
import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.AttendanceTime;
import com.eservice.iot.model.park.Staff;
import com.eservice.iot.model.park.Tag;
import com.eservice.iot.model.user.User;
import com.eservice.iot.model.user.UserTime;
import com.eservice.iot.service.park.AccessService;
import com.eservice.iot.service.park.StaffService;
import com.eservice.iot.service.park.TagService;
import com.eservice.iot.util.Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2018/08/21.
 */
@RestController
@RequestMapping("/staff")
public class StaffController {
    private final static Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Resource
    private StaffService staffService;
    @Resource
    private TagService tagService;
    @Resource
    private AccessService accessService;
    @Value("${excel_path}")
    private String EXCEL_PATH;
    @Value("${work_time}")
    private int WORK_TIME;

    List<AttendanceTime> attendanceTimes;

    private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${park_base_url}")
    private String PARK_BASE_URL;

    @GetMapping("/deleteByTagName")
    public Result deleteStaffByTagName(@RequestParam String name) {
        if (name == null || "".equals(name)) {
            return ResultGenerator.genFailResult("标签名字不能为空！");
        } else {
            if (tagService == null) {
                return ResultGenerator.genFailResult("标签服务没有启动！");
            } else {
                List<Tag> allTagList = tagService.getAllTagList();
                String targetTagId = null;
                for (Tag item : allTagList) {
                    if (item.getTag_name().equals(name)) {
                        targetTagId = item.getTag_id();
                        break;
                    }
                }
                if (targetTagId == null) {
                    return ResultGenerator.genFailResult("找不到标签名字！");
                } else {
                    ArrayList<Staff> allStaffList = staffService.getStaffAllList();
                    ArrayList<Staff> allDeleteStaffList = new ArrayList<>();
                    for (Staff item : allStaffList) {
                        if (item.getTag_id_list().contains(targetTagId)) {
                            allDeleteStaffList.add(item);
                        }
                    }
                    int deleteCount = 0;
                    String resultStr = "";
                    ArrayList<String> failedList = new ArrayList<>();
                    resultStr += "需删除staff总数：" + allDeleteStaffList.size();
                    for (int i = 0; i < allDeleteStaffList.size(); i++) {
                        if (staffService.deleteStaff(allDeleteStaffList.get(i).getStaffId())) {
                            deleteCount++;
                        } else {
                            failedList.add(allDeleteStaffList.get(i).getPerson_information().getName());
                        }
                    }
                    resultStr += "; 删除成功staff总数：" + deleteCount;
                    resultStr += "; 删除失败staff数：" + failedList.size();
                    resultStr += "; 失败列表：" + failedList.toString();
                    logger.warn("Delete staff by manually! Deleted number is {}, Failed number is ", deleteCount, failedList.size());
                    return ResultGenerator.genSuccessResult(resultStr);
                }
            }
        }
    }

    @GetMapping("/excel")
    public Result excel() {
        if (staffService.getStaffAllList().size() > 0) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("staff");

            ///设置要导出的文件的名字
            String fileName = formatter2.format(new Date()) + ".xls";
            //新增数据行，并且设置单元格数据
            insertDataInSheet(workbook, sheet, staffService.getStaffAllList());

            try {
                FileOutputStream out = new FileOutputStream("./" + fileName);
                workbook.write(out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("Staff数量为0");
        }
    }

    private void insertDataInSheet(HSSFWorkbook wb, HSSFSheet sheet, List<Staff> list) {
        int rowNum = 1;
        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        String[] excelHeaders = {"照片", "姓名", "员工号", "卡号", "标签"};
        //headers表示excel表中第一行的表头
        HSSFRow row3 = sheet.createRow(0);
        //在excel表中添加表头
        for (int i = 0; i < excelHeaders.length; i++) {
            HSSFCell cell = row3.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(excelHeaders[i]);
            cell.setCellValue(text);
        }
        //在表中存放查询到的数据放入对应的列
        int index = 1;
        for (Staff staff : list) {
            if (staff.getFace_list().size() > 0) {
                HSSFRow row = sheet.createRow(rowNum);
                row.setHeight((short) 1000);
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                String url = PARK_BASE_URL + "/image/" + staff.getFace_list().get(0).getFace_image_id();
                BufferedImage image = null;
                try {
                    image = ImageIO.read(new URL(url));
                    ImageIO.write(image, "jpg", byteArrayOut);
                    //anchor主要用于设置图片的属性
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, index, (short) 1, index + 1);
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                    patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    index++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //row.createCell(0).setCellValue(staff.getFace_list().get(0).getFace_image_id());
                row.createCell(1).setCellValue(staff.getPerson_information().getName());
                row.createCell(2).setCellValue(staff.getPerson_information().getId());
                if (staff.getCard_numbers() != null) {
                    row.createCell(3).setCellValue(listToString(staff.getCard_numbers()));
                } else {
                    row.createCell(3).setCellValue(staff.getPerson_information().getCard_no());
                }
                row.createCell(4).setCellValue(tagService.getTagName(staff.getTag_id_list()).toString());
                rowNum++;
            } else {
                logger.warn("Face ID list is zero: {}", staff.getPerson_information().getName());
            }
        }
    }

    private String listToString(List<String> list) {
        String result = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    result += list.get(i) + " | ";
                } else {
                    result += list.get(i);
                }
            }
        }
        return result;
    }

    @RequestMapping("/add")
    public Result add(String info) {
        Map map = (Map) JSON.parseObject(info);
        List list = JSON.parseArray(map.get("tag").toString());
        String[] tagNameList = new String[list.size()];
        list.toArray(tagNameList);
        List<String> idList = tagService.getStaffId(tagNameList);
        List imageList = JSON.parseArray(map.get("image").toString());
        map.put("image", imageList);
        map.put("tag", idList);
        map.put("birthday", "");
        return ResultGenerator.genSuccessResult(
                staffService.createStaff(map));
    }

    @PostMapping("queryStaffVisitRerordTime")
    public Result queryStaffVisitRerordTime(@RequestParam Date queryStartTime, @RequestParam Date queryFinishTime, String type, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        Long startTime = queryStartTime.getTime() / 1000;
        Long endTime = queryFinishTime.getTime() / 1000;
        List<UserTime> user = accessService.querySignInVisitor(startTime, endTime, type);
        if (user != null) {
            breakUpData(user);
            PageHelper.startPage(page, size);
            PageInfo pageInfo = new PageInfo(attendanceTimes);
            pageInfo.setList(pagingQuery(page, size));
            return ResultGenerator.genSuccessResult(pageInfo);
        }
        return null;
    }

    public void breakUpData(List<UserTime> userTimes) {
        attendanceTimes = new ArrayList<>();
        for (UserTime entry : userTimes) {
            for (Map.Entry<Integer, Date[]> entryValue : entry.getDay_time().entrySet()) {
                AttendanceTime attendanceTime = new AttendanceTime();
                attendanceTime.setName(entry.getName());
                attendanceTime.setStartTime(entryValue.getValue()[1]);
                attendanceTime.setEndTime(entryValue.getValue()[0]);
                attendanceTimes.add(attendanceTime);
            }
        }
    }

    public List<AttendanceTime> pagingQuery(int page, int size) {
        List<AttendanceTime> attendanceTimeList = new ArrayList<>();
        int start = (page - 1) * size;
        int end = start + size;
        int index = attendanceTimes.size();
        for (int i = start; i < end && i < index; i++) {
            attendanceTimeList.add(attendanceTimes.get(i));
        }
        return attendanceTimeList;
    }


    @PostMapping("/exportRecord")
    public Result exportRecord(@RequestParam Date start_time, @RequestParam Date endTime, @RequestParam String positionType) throws UnknownHostException {
        Long queryTime = start_time.getTime() / 1000;
        Long endDate = endTime.getTime() / 1000;

        //获取本地ip地址
        InetAddress ip = InetAddress.getLocalHost();
        List<UserTime> userTimes = accessService.querySignInVisitor(queryTime, endDate, positionType);
        //设置要导出的文件的名字
        String fileName = "学员通行-" + format.format(new Date()) + positionType + ".xls";

        if (userTimes.size() > 0) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("通行统计");
            //新增数据行，并且设置单元格数据
            insertDataInSheet(workbook, sheet, userTimes, queryTime, endDate);
            try {
                //放excel表格需要存放的地址
                File dir = new File(EXCEL_PATH);
                dir.setWritable(true, false);
                if (!dir.exists()) {
                    if (dir.mkdir()) {
                        logger.info("excel目录创建成功");
                    } else {
                        logger.info("excel目录创建失败");
                    }
                }
                FileOutputStream out = new FileOutputStream(EXCEL_PATH + fileName);
                workbook.write(out);
                out.close();
                logger.info("excel表格创建成功");
            } catch (FileNotFoundException e) {
                logger.info("excel表格创建失败");
                e.printStackTrace();
            } catch (IOException e) {
                logger.info("excel表格创建失败");
                e.printStackTrace();
            }
            return ResultGenerator.genSuccessResult(fileName);
        } else {
            return ResultGenerator.genSuccessResult(fileName);
        }

    }


    private void insertDataInSheet(HSSFWorkbook wk, HSSFSheet sheet, List<UserTime> userTimes, Long startTime, Long endTime) {
        //获取所查月份时间，显示在表格中
        Date startDate = new Date(startTime * 1000L);
        String start = formatter2.format(startDate);
        Date endDate = new Date(endTime * 1000L);
        String end = formatter2.format(endDate);
        /**
         * 设置输出单元格样式
         */
        HSSFCellStyle setStyle = wk.createCellStyle();
        CellRangeAddress region = new CellRangeAddress(0, 1, 0, 30);
        sheet.addMergedRegion(region);
        setStyle.setAlignment(HorizontalAlignment.CENTER);
        HSSFFont font = wk.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 20);
        setStyle.setFont(font);
        setStyle.setWrapText(true);

        String[] excelHeaders = {"刷脸记录表"};
        //headers表示excel表中第一行的表头
        HSSFRow row3 = sheet.createRow(0);
        //在Excel表中添加表头
        for (int i = 0; i < excelHeaders.length; i++) {
            HSSFCell cell = row3.createCell(i);
            cell.setCellStyle(setStyle);
            HSSFRichTextString text = new HSSFRichTextString(excelHeaders[i]);
            cell.setCellValue(text + "   \t\t(" + start + "***" + end + ")");
        }
        //在表中存放查询到的数据放入对应的列
        int rowNum = 3;
        HSSFRow insert = sheet.createRow(2);
        for (int i = 0; i <= 30; i++) {
            insert.createCell(i).setCellValue(i + 1);
        }

        HSSFCellStyle cellStyle = wk.createCellStyle();
        HSSFFont font2 = wk.createFont();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        font2.setFontHeightInPoints((short) 10);
        cellStyle.setFont(font2);
        cellStyle.setWrapText(true);

        for (UserTime entry : userTimes) {
            HSSFRow row = sheet.createRow(rowNum);
            HSSFCell cell = row.createCell(0);
            row.setHeight((short) 700);
            row.setRowStyle(cellStyle);
            cell.setCellValue(entry.getName());
            cell.setCellStyle(setStyle);
            //合并单元格,在名字
            CellRangeAddress reg = new CellRangeAddress(rowNum, rowNum, 0, 31);
            sheet.addMergedRegion(reg);
            HSSFRow row2 = sheet.createRow(rowNum + 1);
            row2.setHeight((short) 700);
            for (Map.Entry<Integer, Date[]> entryValue : entry.getDay_time().entrySet()) {
                HSSFCell cells = row2.createCell(entryValue.getKey() - 1);
                int startWorkTime = entryValue.getValue()[1].getHours();
                int startEndTime = entryValue.getValue()[0].getHours();
                // 当天最早和最晚时间
                if ((startEndTime - startWorkTime) <= WORK_TIME) {
                    cells.setCellValue(sdf.format(entryValue.getValue()[0]).split(" ")[1]);
                } else {
                    cells.setCellValue(sdf.format(entryValue.getValue()[1]).split(" ")[1] + "\n" + sdf.format(entryValue.getValue()[0]).split(" ")[1]);
                }

                cells.setCellStyle(cellStyle);

            }
            rowNum += 2;
        }
    }
}








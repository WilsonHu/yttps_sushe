package com.eservice.iot.model.park;

import java.util.List;
import java.util.Map;

/**
 * @program:yttps
 * @description:
 * @author: Mr.Henry
 * @create:2019/6/18 9:52
 */
public class MapAlert {

    /**
     * score : 0
     * card_number : string
     * device_id : string
     * surveillance_policy_id_list : ["string"]
     * face_image_id : string
     * identity : STRANGER
     * person : {"tag_id_list":["string"],"upload_time":0,"person_information":{"birthday":"string","visit_purpose":"0","remark":"string","identity_number":"string","phone":"string","name":"string","company":"string","id":"string","visitee_name":"string","employed_date":"string","visit_end_timestamp":0,"visit_start_timestamp":0,"visit_time_type":"0"},"face_list":[{"face_image_id":"string","scene_image_id":"string","face_id":"string"}],"identity":"STAFF","meta":{"additionalProp1":{},"additionalProp3":{},"additionalProp2":{}},"card_numbers":["string"],"person_id":"string"}
     * scene_image_id : string
     * face_id : string
     * type : 0
     * record_type : FACE
     * timestamp : 0
     */
    private int score;
    private String card_number;
    private String device_id;
    private List<String> surveillance_policy_id_list;
    private String face_image_id;
    private String identity;
    private PersonEntity person;
    private String scene_image_id;
    private String face_id;
    private String type;
    private String record_type;
    private int timestamp;

    public void setScore(int score) {
        this.score = score;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setSurveillance_policy_id_list(List<String> surveillance_policy_id_list) {
        this.surveillance_policy_id_list = surveillance_policy_id_list;
    }

    public void setFace_image_id(String face_image_id) {
        this.face_image_id = face_image_id;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public void setScene_image_id(String scene_image_id) {
        this.scene_image_id = scene_image_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getScore() {
        return score;
    }

    public String getCard_number() {
        return card_number;
    }

    public String getDevice_id() {
        return device_id;
    }

    public List<String> getSurveillance_policy_id_list() {
        return surveillance_policy_id_list;
    }

    public String getFace_image_id() {
        return face_image_id;
    }

    public String getIdentity() {
        return identity;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public String getScene_image_id() {
        return scene_image_id;
    }

    public String getFace_id() {
        return face_id;
    }

    public String getType() {
        return type;
    }

    public String getRecord_type() {
        return record_type;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public class PersonEntity {
        /**
         * tag_id_list : ["string"]
         * upload_time : 0
         * person_information : {"birthday":"string","visit_purpose":"0","remark":"string","identity_number":"string","phone":"string","name":"string","company":"string","id":"string","visitee_name":"string","employed_date":"string","visit_end_timestamp":0,"visit_start_timestamp":0,"visit_time_type":"0"}
         * face_list : [{"face_image_id":"string","scene_image_id":"string","face_id":"string"}]
         * identity : STAFF
         * meta : {"additionalProp1":{},"additionalProp3":{},"additionalProp2":{}}
         * card_numbers : ["string"]
         * person_id : string
         */
        private List<String> tag_id_list;
        private int upload_time;
        private Person_informationEntity person_information;
        private List<Face_listEntity> face_list;
        private String identity;
        private Map meta;
        private List<String> card_numbers;
        private String person_id;

        public void setTag_id_list(List<String> tag_id_list) {
            this.tag_id_list = tag_id_list;
        }

        public void setUpload_time(int upload_time) {
            this.upload_time = upload_time;
        }

        public void setPerson_information(Person_informationEntity person_information) {
            this.person_information = person_information;
        }

        public void setFace_list(List<Face_listEntity> face_list) {
            this.face_list = face_list;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }



        public void setCard_numbers(List<String> card_numbers) {
            this.card_numbers = card_numbers;
        }

        public void setPerson_id(String person_id) {
            this.person_id = person_id;
        }

        public List<String> getTag_id_list() {
            return tag_id_list;
        }

        public int getUpload_time() {
            return upload_time;
        }

        public Person_informationEntity getPerson_information() {
            return person_information;
        }

        public List<Face_listEntity> getFace_list() {
            return face_list;
        }

        public String getIdentity() {
            return identity;
        }

        public Map getMeta() {
            return meta;
        }

        public void setMeta(Map meta) {
            this.meta = meta;
        }

        public List<String> getCard_numbers() {
            return card_numbers;
        }

        public String getPerson_id() {
            return person_id;
        }

        public class Person_informationEntity {
            /**
             * birthday : string
             * visit_purpose : 0
             * remark : string
             * identity_number : string
             * phone : string
             * name : string
             * company : string
             * id : string
             * visitee_name : string
             * employed_date : string
             * visit_end_timestamp : 0
             * visit_start_timestamp : 0
             * visit_time_type : 0
             */
            private String birthday;
            private String visit_purpose;
            private String remark;
            private String identity_number;
            private String phone;
            private String name;
            private String company;
            private String id;
            private String visitee_name;
            private String employed_date;
            private int visit_end_timestamp;
            private int visit_start_timestamp;
            private String visit_time_type;

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public void setVisit_purpose(String visit_purpose) {
                this.visit_purpose = visit_purpose;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setIdentity_number(String identity_number) {
                this.identity_number = identity_number;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setVisitee_name(String visitee_name) {
                this.visitee_name = visitee_name;
            }

            public void setEmployed_date(String employed_date) {
                this.employed_date = employed_date;
            }

            public void setVisit_end_timestamp(int visit_end_timestamp) {
                this.visit_end_timestamp = visit_end_timestamp;
            }

            public void setVisit_start_timestamp(int visit_start_timestamp) {
                this.visit_start_timestamp = visit_start_timestamp;
            }

            public void setVisit_time_type(String visit_time_type) {
                this.visit_time_type = visit_time_type;
            }

            public String getBirthday() {
                return birthday;
            }

            public String getVisit_purpose() {
                return visit_purpose;
            }

            public String getRemark() {
                return remark;
            }

            public String getIdentity_number() {
                return identity_number;
            }

            public String getPhone() {
                return phone;
            }

            public String getName() {
                return name;
            }

            public String getCompany() {
                return company;
            }

            public String getId() {
                return id;
            }

            public String getVisitee_name() {
                return visitee_name;
            }

            public String getEmployed_date() {
                return employed_date;
            }

            public int getVisit_end_timestamp() {
                return visit_end_timestamp;
            }

            public int getVisit_start_timestamp() {
                return visit_start_timestamp;
            }

            public String getVisit_time_type() {
                return visit_time_type;
            }
        }

        public class Face_listEntity {
            /**
             * face_image_id : string
             * scene_image_id : string
             * face_id : string
             */
            private String face_image_id;
            private String scene_image_id;
            private String face_id;

            public void setFace_image_id(String face_image_id) {
                this.face_image_id = face_image_id;
            }

            public void setScene_image_id(String scene_image_id) {
                this.scene_image_id = scene_image_id;
            }

            public void setFace_id(String face_id) {
                this.face_id = face_id;
            }

            public String getFace_image_id() {
                return face_image_id;
            }

            public String getScene_image_id() {
                return scene_image_id;
            }

            public String getFace_id() {
                return face_id;
            }
        }


    }
}

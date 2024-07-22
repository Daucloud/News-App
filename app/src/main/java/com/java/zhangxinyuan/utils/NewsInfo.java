package com.java.zhangxinyuan.utils;

import java.util.List;

public class NewsInfo {
    private String pageSize;
    private Integer total;
    private List<DataDTO> data;
    private String currentPage;

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public static class DataDTO {
        private String image;
        private String publishTime;
        private List<KeywordsDTO> keywords;
        private String language;
        private String video;
        private String title;
        private List<WhenDTO> when;
        private String content;
        private String url;
        private List<PersonsDTO> persons;
        private String newsID;
        private String crawlTime;
        private List<OrganizationsDTO> organizations;
        private String publisher;
        private List<LocationsDTO> locations;
        private List<?> where;
        private List<?> scholars;
        private String category;
        private List<WhoDTO> who;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public List<KeywordsDTO> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<KeywordsDTO> keywords) {
            this.keywords = keywords;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<WhenDTO> getWhen() {
            return when;
        }

        public void setWhen(List<WhenDTO> when) {
            this.when = when;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<PersonsDTO> getPersons() {
            return persons;
        }

        public void setPersons(List<PersonsDTO> persons) {
            this.persons = persons;
        }

        public String getNewsID() {
            return newsID;
        }

        public void setNewsID(String newsID) {
            this.newsID = newsID;
        }

        public String getCrawlTime() {
            return crawlTime;
        }

        public void setCrawlTime(String crawlTime) {
            this.crawlTime = crawlTime;
        }

        public List<OrganizationsDTO> getOrganizations() {
            return organizations;
        }

        public void setOrganizations(List<OrganizationsDTO> organizations) {
            this.organizations = organizations;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public List<LocationsDTO> getLocations() {
            return locations;
        }

        public void setLocations(List<LocationsDTO> locations) {
            this.locations = locations;
        }

        public List<?> getWhere() {
            return where;
        }

        public void setWhere(List<?> where) {
            this.where = where;
        }

        public List<?> getScholars() {
            return scholars;
        }

        public void setScholars(List<?> scholars) {
            this.scholars = scholars;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<WhoDTO> getWho() {
            return who;
        }

        public void setWho(List<WhoDTO> who) {
            this.who = who;
        }

        public static class KeywordsDTO {
            private Double score;
            private String word;

            public Double getScore() {
                return score;
            }

            public void setScore(Double score) {
                this.score = score;
            }

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }
        }

        public static class WhenDTO {
            private Double score;
            private String word;

            public Double getScore() {
                return score;
            }

            public void setScore(Double score) {
                this.score = score;
            }

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }
        }

        public static class PersonsDTO {
            private Integer count;
            private String linkedURL;
            private String mention;

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }

            public String getLinkedURL() {
                return linkedURL;
            }

            public void setLinkedURL(String linkedURL) {
                this.linkedURL = linkedURL;
            }

            public String getMention() {
                return mention;
            }

            public void setMention(String mention) {
                this.mention = mention;
            }
        }

        public static class OrganizationsDTO {
            private Integer count;
            private String linkedURL;
            private String mention;

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }

            public String getLinkedURL() {
                return linkedURL;
            }

            public void setLinkedURL(String linkedURL) {
                this.linkedURL = linkedURL;
            }

            public String getMention() {
                return mention;
            }

            public void setMention(String mention) {
                this.mention = mention;
            }
        }

        public static class LocationsDTO {
            private Double lng;
            private Integer count;
            private String linkedURL;
            private Double lat;
            private String mention;

            public Double getLng() {
                return lng;
            }

            public void setLng(Double lng) {
                this.lng = lng;
            }

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }

            public String getLinkedURL() {
                return linkedURL;
            }

            public void setLinkedURL(String linkedURL) {
                this.linkedURL = linkedURL;
            }

            public Double getLat() {
                return lat;
            }

            public void setLat(Double lat) {
                this.lat = lat;
            }

            public String getMention() {
                return mention;
            }

            public void setMention(String mention) {
                this.mention = mention;
            }
        }

        public static class WhoDTO {
            private Double score;
            private String word;

            public Double getScore() {
                return score;
            }

            public void setScore(Double score) {
                this.score = score;
            }

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }
        }
    }
}

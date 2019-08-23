package com.ailiaovideo.videoline.json;

public class JsonDoRequestGetOssInfo extends JsonRequestBase{


    /**
     * bucket : videoline
     * domain : http://p4ulgsz1p.bkt.clouddn.com
     * token : fe-37HEsiNTNA-6qSIB57ZTPBuy-FNdrUEpe8SZM:7NABp5wZL-jXsVxtfqtwkIcE8c8=:eyJzY29wZSI6InZpZGVvbGluZSIsImRlYWRsaW5lIjoxNTM0OTU4MTgzfQ==
     */

    private String bucket;
    private String domain;
    private String token;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

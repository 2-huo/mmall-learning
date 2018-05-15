package com.mmall.pojo;

import java.util.Date;

public class User {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String question;

    private String answer;

    private String role;

    private Date createTime;

    private Date updateTime;

    private Boolean enable;

    private String name;

    private String phone;

    private String province;

    private String city;

    private String district;

    private String addr;

    private String lvl;

    private String shopname;

    private Integer shopId;

//    头像 0511
    private String avatar;

    public User(Integer id, String username, String password, String email, String question, String answer, String role, Date createTime, Date updateTime, Boolean enable, String name, String phone, String province, String city, String district, String addr, String lvl, String shopname, Integer shopId, String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.question = question;
        this.answer = answer;
        this.role = role;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enable = enable;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.city = city;
        this.district = district;
        this.addr = addr;
        this.lvl = lvl;
        this.shopname = shopname;
        this.shopId = shopId;

        this.avatar = avatar;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone == null ? null : phone.trim();}


    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
    }

    public String getLvl() {
        return lvl;
    }
    public void setLvl(String lvl) {
        this.lvl = lvl;
    }

    public String getShopname() {return shopname; }
    public void setShopname(String shopname ) {this.shopname = shopname; }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getAvatar() { return avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar == null ? null : avatar.trim(); }

}
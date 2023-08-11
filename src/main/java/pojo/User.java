package pojo;

import java.util.Objects;

public class User {
    private String userId;
    private String userName;
    private String password;
    private int isFreezing;
    private String createTime;
    private String updateTime;
    private String email;
    private Integer followNum;//关注数
    private Integer fansNum;//粉丝
    private Integer collectionNum;
    private Integer flag;//用户查看作者时用到,判断用户是否被查看其的用户所关注

    public User() {
    }

    public User(String userId, String userName, int isFreezing, String createTime) {
        this.userId = userId;
        this.userName = userName;
        this.isFreezing = isFreezing;
        this.createTime = createTime;
    }

    public User(String userId, String userName, String password, int isFreezing, String createTime, String updateTime, String email) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.isFreezing = isFreezing;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.email = email;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsFreezing() {
        return isFreezing;
    }

    public void setIsFreezing(int isFreezing) {
        this.isFreezing = isFreezing;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(Integer collectionNum) {
        this.collectionNum = collectionNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isFreezing == user.isFreezing && Objects.equals(userId, user.userId) && Objects.equals(userName, user.userName) && Objects.equals(password, user.password) && Objects.equals(createTime, user.createTime) && Objects.equals(updateTime, user.updateTime) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, password, isFreezing, createTime, updateTime, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", isFreezing=" + isFreezing +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", email='" + email + '\'' +
                '}';
    }
}

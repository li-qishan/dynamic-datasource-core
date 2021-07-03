package com.johns.dynamicdatasource.datasource.entities;

import java.util.Date;

public class Msg {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jsh_msg.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jsh_msg.msg_title
     *
     * @mbggenerated
     */
    private String msgTitle;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jsh_msg.msg_content
     *
     * @mbggenerated
     */
    private String msgContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jsh_msg.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jsh_msg.type
     *
     * @mbggenerated
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jsh_msg.status
     *
     * @mbggenerated
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jsh_msg.tenant_id
     *
     * @mbggenerated
     */
    private Long tenantId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jsh_msg.delete_Flag
     *
     * @mbggenerated
     */
    private String deleteFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jsh_msg.id
     *
     * @return the value of jsh_msg.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jsh_msg.id
     *
     * @param id the value for jsh_msg.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jsh_msg.msg_title
     *
     * @return the value of jsh_msg.msg_title
     *
     * @mbggenerated
     */
    public String getMsgTitle() {
        return msgTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jsh_msg.msg_title
     *
     * @param msgTitle the value for jsh_msg.msg_title
     *
     * @mbggenerated
     */
    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle == null ? null : msgTitle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jsh_msg.msg_content
     *
     * @return the value of jsh_msg.msg_content
     *
     * @mbggenerated
     */
    public String getMsgContent() {
        return msgContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jsh_msg.msg_content
     *
     * @param msgContent the value for jsh_msg.msg_content
     *
     * @mbggenerated
     */
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jsh_msg.create_time
     *
     * @return the value of jsh_msg.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jsh_msg.create_time
     *
     * @param createTime the value for jsh_msg.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jsh_msg.type
     *
     * @return the value of jsh_msg.type
     *
     * @mbggenerated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jsh_msg.type
     *
     * @param type the value for jsh_msg.type
     *
     * @mbggenerated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jsh_msg.status
     *
     * @return the value of jsh_msg.status
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jsh_msg.status
     *
     * @param status the value for jsh_msg.status
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jsh_msg.tenant_id
     *
     * @return the value of jsh_msg.tenant_id
     *
     * @mbggenerated
     */
    public Long getTenantId() {
        return tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jsh_msg.tenant_id
     *
     * @param tenantId the value for jsh_msg.tenant_id
     *
     * @mbggenerated
     */
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jsh_msg.delete_Flag
     *
     * @return the value of jsh_msg.delete_Flag
     *
     * @mbggenerated
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jsh_msg.delete_Flag
     *
     * @param deleteFlag the value for jsh_msg.delete_Flag
     *
     * @mbggenerated
     */
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }
}
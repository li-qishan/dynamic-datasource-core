package com.johns.dynamicdatasource.datasource.mappers;

import com.johns.dynamicdatasource.datasource.entities.Msg;
import com.johns.dynamicdatasource.datasource.entities.MsgExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MsgMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    int countByExample(MsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    int deleteByExample(MsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    int insert(Msg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    int insertSelective(Msg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    List<Msg> selectByExample(MsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    Msg selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Msg record, @Param("example") MsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Msg record, @Param("example") MsgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Msg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jsh_msg
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Msg record);
}
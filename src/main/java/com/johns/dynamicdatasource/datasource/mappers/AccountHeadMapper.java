package com.johns.dynamicdatasource.datasource.mappers;

import com.johns.dynamicdatasource.datasource.entities.AccountHead;
import com.johns.dynamicdatasource.datasource.entities.AccountHeadExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountHeadMapper {
    long countByExample(AccountHeadExample example);

    int deleteByExample(AccountHeadExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AccountHead record);

    int insertSelective(AccountHead record);

    List<AccountHead> selectByExample(AccountHeadExample example);

    AccountHead selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AccountHead record, @Param("example") AccountHeadExample example);

    int updateByExample(@Param("record") AccountHead record, @Param("example") AccountHeadExample example);

    int updateByPrimaryKeySelective(AccountHead record);

    int updateByPrimaryKey(AccountHead record);
}
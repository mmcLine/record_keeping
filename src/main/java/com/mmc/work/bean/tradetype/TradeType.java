package com.mmc.work.bean.tradetype;

import com.mmc.work.base.ExtName;
import com.mmc.work.base.IdBase;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class TradeType extends IdBase implements ExtName {

    private String typeName;

    @Override
    public String name() {
        return getTypeName();
    }

    @Override
    public String fldName() {
        return "type_name";
    }
}

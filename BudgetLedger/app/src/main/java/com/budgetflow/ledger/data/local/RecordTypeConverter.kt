package com.budgetflow.ledger.data.local

import androidx.room.TypeConverter
import com.budgetflow.ledger.domain.model.RecordType

class RecordTypeConverter {

    @TypeConverter
    fun fromRecordType(recordType: RecordType): String {
        return recordType.name
    }

    @TypeConverter
    fun toRecordType(value: String): RecordType {
        return RecordType.valueOf(value)
    }
}

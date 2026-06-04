package rs.edu.raf.rma.core.db.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class DateConverters {
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)
}

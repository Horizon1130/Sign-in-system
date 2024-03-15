package com.myway.platform.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Datetime {

    private LocalDateTime _datetime;

    public Datetime() {
        _datetime = LocalDateTime.now();
    }

    public Datetime(LocalDateTime _datetime) {
        this._datetime = _datetime;
    }

    public Datetime(LocalDate _date) {
        this._datetime = _date.atStartOfDay();
    }

    public Datetime(Object _data) {
        if (_data instanceof LocalDateTime) {
            this._datetime = (LocalDateTime) _data;
        } else if (_data instanceof LocalDate) {
            this._datetime = ((LocalDate) _data).atStartOfDay();
        }
    }

    public static Datetime parse(String datetime, String pattern) {
        if (datetime.length() > 19) {
            datetime = datetime.substring(0, 19);
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        if (pattern.length() == 10) {
            LocalDate parse = LocalDate.parse(datetime, df);
            return new Datetime(parse);
        }
        LocalDateTime parse = LocalDateTime.parse(datetime, df);
        return new Datetime(parse);
    }

    public LocalDate getLocalDate(){
        return _datetime.toLocalDate();
    }

    public LocalDateTime getFullDatetime() {
        return _datetime;
    }

    public int getDate() {
        return new Integer(toString("yyyyMMdd"));
    }

    /**
     * 获取时间戳
     * @return 时间戳
     */
    public long getTimeStamp(){
        return this._datetime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public Datetime addHours(Integer hour){
        this._datetime = this._datetime.plusHours(hour);
        return this;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(_datetime);
    }

    public String toString(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(_datetime);
    }
}

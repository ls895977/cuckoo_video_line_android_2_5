package com.ailiaovideo.videoline.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ailiaovideo.videoline.modle.ReadFile;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "READ_FILE".
*/
public class ReadFileDao extends AbstractDao<ReadFile, Long> {

    public static final String TABLENAME = "READ_FILE";

    /**
     * Properties of entity ReadFile.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FileName = new Property(1, String.class, "fileName", false, "FILE_NAME");
        public final static Property FilePath = new Property(2, String.class, "filePath", false, "FILE_PATH");
        public final static Property FileSize = new Property(3, String.class, "fileSize", false, "FILE_SIZE");
    };


    public ReadFileDao(DaoConfig config) {
        super(config);
    }
    
    public ReadFileDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"READ_FILE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FILE_NAME\" TEXT," + // 1: fileName
                "\"FILE_PATH\" TEXT," + // 2: filePath
                "\"FILE_SIZE\" TEXT);"); // 3: fileSize
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"READ_FILE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ReadFile entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String fileName = entity.getFileName();
        if (fileName != null) {
            stmt.bindString(2, fileName);
        }
 
        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(3, filePath);
        }
 
        String fileSize = entity.getFileSize();
        if (fileSize != null) {
            stmt.bindString(4, fileSize);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ReadFile entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String fileName = entity.getFileName();
        if (fileName != null) {
            stmt.bindString(2, fileName);
        }
 
        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(3, filePath);
        }
 
        String fileSize = entity.getFileSize();
        if (fileSize != null) {
            stmt.bindString(4, fileSize);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ReadFile readEntity(Cursor cursor, int offset) {
        ReadFile entity = new ReadFile( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // fileName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // filePath
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // fileSize
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ReadFile entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFileName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFilePath(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFileSize(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ReadFile entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ReadFile entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

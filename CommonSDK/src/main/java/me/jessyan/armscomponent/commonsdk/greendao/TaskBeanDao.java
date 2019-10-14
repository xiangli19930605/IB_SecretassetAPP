package me.jessyan.armscomponent.commonsdk.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TASK_BEAN".
*/
public class TaskBeanDao extends AbstractDao<TaskBean, Long> {

    public static final String TABLENAME = "TASK_BEAN";

    /**
     * Properties of entity TaskBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Taskid = new Property(1, String.class, "taskid", false, "TASKID");
        public final static Property State = new Property(2, int.class, "state", false, "STATE");
        public final static Property CreateTime = new Property(3, String.class, "createTime", false, "CREATE_TIME");
        public final static Property FinishTime = new Property(4, String.class, "finishTime", false, "FINISH_TIME");
        public final static Property Number = new Property(5, int.class, "number", false, "NUMBER");
        public final static Property PassFlag = new Property(6, int.class, "passFlag", false, "PASS_FLAG");
        public final static Property Reason = new Property(7, String.class, "reason", false, "REASON");
    }


    public TaskBeanDao(DaoConfig config) {
        super(config);
    }
    
    public TaskBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TASK_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TASKID\" TEXT," + // 1: taskid
                "\"STATE\" INTEGER NOT NULL ," + // 2: state
                "\"CREATE_TIME\" TEXT," + // 3: createTime
                "\"FINISH_TIME\" TEXT," + // 4: finishTime
                "\"NUMBER\" INTEGER NOT NULL ," + // 5: number
                "\"PASS_FLAG\" INTEGER NOT NULL ," + // 6: passFlag
                "\"REASON\" TEXT);"); // 7: reason
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TASK_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TaskBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String taskid = entity.getTaskid();
        if (taskid != null) {
            stmt.bindString(2, taskid);
        }
        stmt.bindLong(3, entity.getState());
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(4, createTime);
        }
 
        String finishTime = entity.getFinishTime();
        if (finishTime != null) {
            stmt.bindString(5, finishTime);
        }
        stmt.bindLong(6, entity.getNumber());
        stmt.bindLong(7, entity.getPassFlag());
 
        String reason = entity.getReason();
        if (reason != null) {
            stmt.bindString(8, reason);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TaskBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String taskid = entity.getTaskid();
        if (taskid != null) {
            stmt.bindString(2, taskid);
        }
        stmt.bindLong(3, entity.getState());
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(4, createTime);
        }
 
        String finishTime = entity.getFinishTime();
        if (finishTime != null) {
            stmt.bindString(5, finishTime);
        }
        stmt.bindLong(6, entity.getNumber());
        stmt.bindLong(7, entity.getPassFlag());
 
        String reason = entity.getReason();
        if (reason != null) {
            stmt.bindString(8, reason);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TaskBean readEntity(Cursor cursor, int offset) {
        TaskBean entity = new TaskBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // taskid
            cursor.getInt(offset + 2), // state
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // createTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // finishTime
            cursor.getInt(offset + 5), // number
            cursor.getInt(offset + 6), // passFlag
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // reason
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TaskBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTaskid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setState(cursor.getInt(offset + 2));
        entity.setCreateTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFinishTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNumber(cursor.getInt(offset + 5));
        entity.setPassFlag(cursor.getInt(offset + 6));
        entity.setReason(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TaskBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TaskBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TaskBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

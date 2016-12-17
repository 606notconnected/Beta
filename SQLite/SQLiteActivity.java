package SQLite;
 
import  SQLiteDb.StuDBHelper;
 
import android.app.Activity;  
import android.content.ContentValues;  
import android.database.Cursor;  
import android.database.sqlite.SQLiteDatabase;  
import android.os.Bundle;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
/*  
 * @author lingdududu  
 */ 
public class SQLiteActivity extends Activity {  
    /** Called when the activity is first created. */ 
    //����������ť  
    private Button createBtn;  
    private Button insertBtn;  
    private Button updateBtn;  
    private Button queryBtn;  
    private Button deleteBtn;  
    private Button ModifyBtn;  
    @Override 
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
          
        //����creatView����  
        creatView();  
        //setListener����  
        setListener();          
    }  
      
    //ͨ��findViewById���Button����ķ���  
    private void creatView(){  
        createBtn = (Button)findViewById(R.id.createDatabase);  
        updateBtn = (Button)findViewById(R.id.updateDatabase);  
        insertBtn = (Button)findViewById(R.id.insert);  
        ModifyBtn = (Button)findViewById(R.id.update);  
        queryBtn = (Button)findViewById(R.id.query);  
        deleteBtn = (Button)findViewById(R.id.delete);  
    }  
      
    //Ϊ��ťע������ķ���  
    private void setListener(){  
        createBtn.setOnClickListener(new CreateListener());  
        updateBtn.setOnClickListener(new UpdateListener());  
        insertBtn.setOnClickListener(new InsertListener());  
        ModifyBtn.setOnClickListener(new  ModifyListener());  
        queryBtn.setOnClickListener(new QueryListener());  
        deleteBtn.setOnClickListener(new DeleteListener());  
    }  
      
    //�������ݿ�ķ���  
    class CreateListener implements OnClickListener{  
 
        @Override 
        public void onClick(View v) {  
            //����StuDBHelper����  
            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db",null,1);  
            //�õ�һ���ɶ���SQLiteDatabase����  
            SQLiteDatabase db =dbHelper.getReadableDatabase();  
        }         
    }  
      
    //�������ݿ�ķ���  
    class UpdateListener implements OnClickListener{  
 
        @Override 
        public void onClick(View v) {  
            // ���ݿ�汾�ĸ���,��ԭ����1��Ϊ2  
            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db",null,2);  
            SQLiteDatabase db =dbHelper.getReadableDatabase();  
        }         
    }  
      
    //�������ݵķ���  
    class InsertListener implements OnClickListener{  
 
        @Override 
        public void onClick(View v) {  
 
            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db",null,1);  
            //�õ�һ����д�����ݿ�  
            SQLiteDatabase db =dbHelper.getWritableDatabase();  
              
            //����ContentValues���� //key:������value:������ֵ    
            ContentValues cv = new ContentValues();  
            //��ContentValues���������ݣ���-ֵ��ģʽ  
            cv.put("id", 1);  
            cv.put("sname", "xiaoming");  
            cv.put("sage", 21);  
            cv.put("ssex", "male");  
            //����insert�����������ݲ������ݿ�  
            db.insert("stu_table", null, cv);  
            //�ر����ݿ�  
            db.close();  
        }         
    }  
      
    //��ѯ���ݵķ���  
    class QueryListener implements OnClickListener{  
 
        @Override 
        public void onClick(View v) {  
 
            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db",null,1);  
            //�õ�һ����д�����ݿ�  
            SQLiteDatabase db =dbHelper.getReadableDatabase();  
            //����1������    
            //����2��Ҫ����ʾ����    
            //����3��where�Ӿ�    
            //����4��where�Ӿ��Ӧ������ֵ    
            //����5�����鷽ʽ    
            //����6��having����    
            //����7������ʽ    
            Cursor cursor = db.query("stu_table", new String[]{"id","sname","sage","ssex"}, "id=?", new String[]{"1"}, null, null, null);  
            while(cursor.moveToNext()){  
                String name = cursor.getString(cursor.getColumnIndex("sname"));  
                String age = cursor.getString(cursor.getColumnIndex("sage"));  
                String sex = cursor.getString(cursor.getColumnIndex("ssex"));  
                System.out.println("query------->" + "������"+name+" "+"���䣺"+age+" "+"�Ա�"+sex);  
            }  
            //�ر����ݿ�  
            db.close();  
        }         
    }  
      
    //�޸����ݵķ���  
    class ModifyListener implements OnClickListener{  
 
        @Override 
        public void onClick(View v) {  
 
            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db",null,1);  
            //�õ�һ����д�����ݿ�  
            SQLiteDatabase db =dbHelper.getWritableDatabase();  
            ContentValues cv = new ContentValues();  
            cv.put("sage", "23");  
            //where �Ӿ� "?"��ռλ���ţ���Ӧ�����"1",  
            String whereClause="id=?";  
            String [] whereArgs = {String.valueOf(1)};  
            //����1 ��Ҫ���µı���  
            //����2 ��һ��ContentValeus����  
            //����3 ��where�Ӿ�  
            db.update("stu_table", cv, whereClause, whereArgs);  
        }  
    }  
      
    //ɾ�����ݵķ���  
    class DeleteListener implements OnClickListener{  
 
        @Override 
        public void onClick(View v) {  
 
            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db",null,1);  
            //�õ�һ����д�����ݿ�  
            SQLiteDatabase db =dbHelper.getReadableDatabase();  
            String whereClauses = "id=?";  
            String [] whereArgs = {String.valueOf(2)};  
            //����delete������ɾ������   
            db.delete("stu_table", whereClauses, whereArgs);  
        }     
    }  
} 
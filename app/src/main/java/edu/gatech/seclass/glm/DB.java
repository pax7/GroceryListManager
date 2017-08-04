package edu.gatech.seclass.glm;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.Deflater;


/**
 * Used to help with DB access.
 * http://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application
 * http://www.drdobbs.com/database/using-sqlite-on-android/232900584?pgno=1
 * https://developer.android.com/training/basics/data-storage/databases.html#DbHelper
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
class DB extends SQLiteOpenHelper {

    private static String DB_PATH = "";
    private static String DB_NAME = "glm.db";
    private SQLiteDatabase _db;
    private static DB _instance=new DB();

    /**
     * Constructs a DB helper.  No need to call it but it can't be private due to extending SQLiteOpenHelper.
     */
    DB() {
        super(App.getContext(), DB_NAME, null, 1);
        _instance=this;
        DB_PATH = "/data/data/" + "edu.gatech.seclass.glm" + "/databases/";
        createDatabase();
    }

    /**
     * Method included to allow AndroidDatabaseManager to do inspection
     * @param Query query
     * @return cursors
     */
    ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(Query, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the ArrayList
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the ArrayList
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }


    /**
     * Gets a readable DB
     * @return readable DB
     */
    static SQLiteDatabase getReadableDB() {return _instance.getReadableDatabase();}

    /**
     * Gets a writable DB
     * @return writable DB
     */
    static SQLiteDatabase getWritableDB() { return _instance.getWritableDatabase();}

    //static{resetDataBase();}

    private static void createDatabase() throws SQLException {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            resetDataBase();
        }
        _instance.openDataBase();
        _instance.close();
    }

    private static byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }

    /**
     * Returns a compressed string of the DB
     * @return the DB
     */
    static String getDump()
    {
        try
        {
            String dbFileName = DB_PATH + DB_NAME;
            byte[] data =fullyReadFileToBytes(new File(dbFileName));
            Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
            deflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            deflater.finish();
            byte[] buffer = new byte[1024];
            while (!deflater.finished())
            {
                int count = deflater.deflate(buffer); // returns the generated code... index
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            byte[] output = outputStream.toByteArray();
            return Base64.encodeToString(output,Base64.NO_WRAP);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    /**
     * Removes all data from the DB except the ItemTypes
     */
    static void resetDataBase()
    {
        _instance.getReadableDatabase();
        _instance.close();
        try {
            _instance.copyDataBase(DB_NAME);
        } catch (IOException exIO) {
            throw new Error("ErrorCopyingDataBase");
        }
    }

    /**
     * Loads the test DB.
     */
    static void loadTestDataBase()
    {
        try
        { _instance.copyDataBase("testglm.db");}
        catch (Exception e){}
    }



    private void copyDataBase(String origin) throws IOException {
        InputStream inputStream = App.getContext().getAssets().open(origin);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream outputStream = new FileOutputStream(outFileName);
        byte[] byteArray = new byte[1024];
        int length;
        while ((length = inputStream.read(byteArray)) > 0) {
            outputStream.write(byteArray, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    /**
     * Open a DB connection
     * @throws SQLException
     */
    private void openDataBase() throws SQLException {
        String dbPath = DB_PATH + DB_NAME;
        _db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    /**
     * Closes the DB connection.
     */
    @Override
    public synchronized void close() {
        if (_db != null)
            _db.close();
        super.close();
    }

    /**
     * Unused
     * @param sqLiteDatabase db
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    /**
     * Unused
     * @param sqLiteDatabase
     * @param x
     * @param y
     */
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int x,int y) {}


}
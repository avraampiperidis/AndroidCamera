package com.protectsoft.simplecam.test;

import com.protectsoft.simplecam.bucket.BucketFiles;

import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;

/**
 */
@Deprecated //most of them needs update!
public class BucketFilesTest extends TestCase {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        //delete/remove6 files testing
        BucketFiles.deteleAllFiles();
        BucketFiles.clearPictureFiles();
        assertEquals(BucketFiles.getPictureFileSize(), 0);

        //random file generation test
        //100 files

        String test = "genfiletest";
        for(int i = 0; i < 50; i++) {
            System.out.println("create"+i);
            assertEquals(i, BucketFiles.getPictureFileSize());
            File file = new File(test+i);
            file.createNewFile();
            BucketFiles.addPictureFile(file);
        }

        for(int i = 49; i >= 0; i--) {
            System.out.println("delete"+i);
            int y = i;
            assertEquals(++y,BucketFiles.getPictureFileSize());
            BucketFiles.deleteFileFromFileSystem(new File(test+i));
        }

        BucketFiles.deteleAllFiles();
        BucketFiles.clearPictureFiles();
        assertEquals(BucketFiles.getPictureFileSize(), 0);


    }


    public void setUp() throws Exception {
        super.setUp();
        File file = new File("file1");
        file.createNewFile();
        BucketFiles.addPictureFile(file);

        file = new File("file2");
        file.createNewFile();
        BucketFiles.addPictureFile(file);

        file = new File("file3");
        file.createNewFile();
        BucketFiles.addPictureFile(file);

        file = new File("file4");
        file.createNewFile();
        BucketFiles.addPictureFile(file);

        file = new File("file5");
        file.createNewFile();
        BucketFiles.addPictureFile(file);

        file = new File("file6");
        file.createNewFile();
        BucketFiles.addPictureFile(file);
    }

    public void tearDown() throws Exception {
        BucketFiles.deteleAllFiles();
        BucketFiles.clearPictureFiles();
        assertEquals(BucketFiles.getPictureFileSize(), 0);

        //random file generation test
        //100 files

        String test = "genfiletest";
        for(int i = 0; i < 50; i++) {
            System.out.println("create"+i);
            assertEquals(i, BucketFiles.getPictureFileSize());
            File file = new File(test+i);
            file.createNewFile();
            BucketFiles.addPictureFile(file);
        }

        for(int i = 49; i >= 0; i--) {
            System.out.println("delete" + i);
            int y = i;
            assertEquals(++y,BucketFiles.getPictureFileSize());
            BucketFiles.deleteFileFromFileSystem(new File(test+i));
        }

        BucketFiles.deteleAllFiles();
        BucketFiles.clearPictureFiles();
        assertEquals(BucketFiles.getPictureFileSize(), 0);
    }



    public void testIsPictureFileListEmpty() throws Exception {
        boolean expected = false;
        boolean actual = BucketFiles.isPictureFileListEmpty();
        assertEquals(expected,actual);
    }


    public void testRemovePictureFile() throws Exception {
        File file = new File("test");
        int beforeadd = BucketFiles.getPictureFileSize();
        BucketFiles.addPictureFile(file);
        BucketFiles.removePictureFile(file);
        int afterremove = BucketFiles.getPictureFileSize();
        assertEquals(beforeadd,afterremove);

    }


    public void testGetNextPictureFile() throws Exception {
        File file = new File("file3");
        assertEquals(file,BucketFiles.getNextPictureFile(new File("file2")));
        file = new File("file2");
        assertEquals(file,BucketFiles.getNextPictureFile(new File("file1")));
        file = null;
        assertEquals(file, BucketFiles.getNextPictureFile(new File("file")));
        file = null;
        assertEquals(file,BucketFiles.getNextPictureFile(new File("file6")));
        file = null;
        assertEquals(file,BucketFiles.getNextPictureFile(new File("filenot in list")));
    }

    public void testGetPreviusPictureFile() throws Exception {
        File file = new File("file1");
        assertEquals(file,BucketFiles.getPreviusPictureFile(new File("file2")));
        file = new File("file4");
        assertEquals(file,BucketFiles.getPreviusPictureFile(new File("file5")));
        file = null;
        assertEquals(file, BucketFiles.getPreviusPictureFile(new File("file7")));
        file = null;
        assertEquals(file, BucketFiles.getPreviusPictureFile(new File("file1")));
        file = null;
        assertEquals(file, BucketFiles.getPreviusPictureFile(new File("not in list")));

    }


    public void testIsFileExists() throws Exception {
        File file = new File("test1");
        file.createNewFile();
        boolean expected = true;
        BucketFiles.addPictureFile(file);
        assertEquals(expected, BucketFiles.isFileExists(file));
        file = new File("test2");
        expected = false;
        assertEquals(expected,BucketFiles.isFileExists(file));
        BucketFiles.deleteFileFromFileSystem(new File("test1"));
    }

    public void testDeleteFileFromFileSystem() throws Exception {
        File file = new File("test3");
        file.createNewFile();
        BucketFiles.addPictureFile(file);
        BucketFiles.deleteFileFromFileSystem(file);
        boolean expected = false;
        assertEquals(expected,BucketFiles.isFileExists(file));

    }




}
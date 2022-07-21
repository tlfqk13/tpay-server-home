package com.tpay.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public boolean compress(String path, String outputPath, String outputFileName) throws Exception {

        // 파일 압축 성공 여부
        boolean isSuccess = false;
        File file = new File(path);

        // 파일의 .zip 이 없는 경우 .zip 을 붙여준다.
        int pos = outputFileName.lastIndexOf(".") == -1 ? outputFileName.length() : outputFileName.lastIndexOf(".");

        // outputFileName .zip 이 없는 경우
        if(!outputFileName.substring(pos).equalsIgnoreCase(".zip")){
            outputFileName += ".zip";
        }

        if (!file.exists()){
            throw new Exception("Not file");
        }

        // 출력 스트림
        FileOutputStream fos = null;
        // 압축 스트림
        ZipOutputStream zos = null;

        try {
            fos = new FileOutputStream(new File(outputPath + outputFileName));
            zos = new ZipOutputStream(fos);

            searchDirectory(file,file.getPath(),zos);

            // 압축 성공
            isSuccess = true;
        }catch (Throwable e){
            throw e;
        }finally {
            if(zos != null)zos.close();
            if(fos != null)fos.close();
        }
        return isSuccess;
    }

    private void searchDirectory(File file, String root, ZipOutputStream zos) throws Exception{
        // 지정된 파일이 디렉토리인지 파일인지 검색
        if(file.isDirectory()){
            // 디렉토리일 경우 재탐색(재귀)
            File[] files = file.listFiles();
            for(File f : files){
                System.out.println("file -> " + f);
                searchDirectory(f,root,zos);
            }
        }else{
            try{
                compressZip(file,root,zos);
            }catch (Throwable e){
                e.printStackTrace();
            }
        }
    }

    private void compressZip(File file, String root, ZipOutputStream zos)throws Throwable{
        FileInputStream fis = null;
        try{
            String zipName = "/excelExportTest.xlsx";
            // 파일을 읽어드림
            fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(zipName);
            // 스트림에 밀어넣기
            zos.putNextEntry(zipEntry);
            int length = (int) file.length();
            byte[] buffer = new byte[length];
            // 스트림읽어드리기
            fis.read(buffer,0,length);
            // 스트림작성
            zos.write(buffer,0,length);
            // 스트림 닫기
            zos.closeEntry();
        }catch (Throwable e){
            throw e;
        }finally {
            if(fis != null)fis.close();
        }
    }
}


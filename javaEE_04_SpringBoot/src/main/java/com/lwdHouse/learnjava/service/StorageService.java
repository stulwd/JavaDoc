package com.lwdHouse.learnjava.service;

import java.io.IOException;
import java.io.InputStream;

interface StorageService {
    // 更具uri打开InputStream
    InputStream openInputStream(String uri) throws IOException;

    // 根据扩展名+inputStream保存并返回URI
    String store(String extName, InputStream input) throws IOException;
}

package com.example.register.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface Readable {
    List read(MultipartFile file) throws IOException;
}

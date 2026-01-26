package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import java.io.File;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    // 저장 후 생성된 메타 정보 반환 (웹 업로드용)
    FileMeta save(MultipartFile file) throws IOException;

    // 저장 후 생성된 메타 정보 반환 (서버 내부 파일용 - 백업 등)
    FileMeta save(File file, String fileName, String contentType) throws IOException;

    // 다운로드 기능
    Resource downloadFile(Long fileId);

    // 메타 정보 조회
    /*
    * FileRepository에 선언해도 되지만, Repository에 작성하면 Controller에서 orElseThrow 로직을 작성해야하기 때문에,
    * Controller는 HTTP 통신만 하면 되는데 로직적인 부분까지 담당하게 되는 것 같아서 Service에서 선언하고 구현
    * */
    FileMeta getFileMeta(Long fileId);

}

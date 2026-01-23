package com.sprint.mission.sb8hrbankteamquerity.mapper;

import com.sprint.mission.sb8hrbankteamquerity.dto.fileMeta.FileMetaResponse;
import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMetaMapper {
    FileMetaResponse toFileMetaResponse(FileMeta fileMeta);
}

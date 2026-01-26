package com.sprint.mission.sb8hrbankteamquerity.mapper;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FileMetaMapper.class})
public interface BackupHistoryMapper {

    @Mapping(target = "fileId", source = "fileMeta.id")
    @Mapping(target = "status", expression = "java(backupHistory.getStatus().name())")
    BackupHistoryDto toDto(BackupHistory backupHistory);

}

package com.sprint.mission.sb8hrbankteamquerity.dto.department;

import java.util.Date;

public record DepartmentUpdateRequest(
    String newName,
    String newDescription,
    Date newEstablishedDate
) {

}

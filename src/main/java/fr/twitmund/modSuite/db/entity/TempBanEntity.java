package fr.twitmund.modSuite.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity
@DatabaseTable(tableName = "TempBan")
public class TempBanEntity {
    @Id
    @DatabaseField(generatedId = true)
    private int tempBan_id;

    @DatabaseField
    private long banDate;

    @DatabaseField
    private long banEndDate;

    @DatabaseField
    private boolean isRevoked;

    @DatabaseField
    private boolean isBannedIp;

    @DatabaseField
    private String revokeReason;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity revokeAuthor;

    @DatabaseField
    private long revokeDate;

    @DatabaseField
    private String banReason;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity playerAuthor;


    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity playerBanned;

    public TempBanEntity() {
    }
}

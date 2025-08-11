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
@DatabaseTable(tableName = "mutes")
public class MuteEntity {

    @Id
    @DatabaseField(generatedId = true)
    private int mute_id;

    @DatabaseField
    private String muteReason;

    @DatabaseField
    private long muteDate;

    @DatabaseField
    private boolean isRevoked;

    @DatabaseField
    private boolean isPermanent;

    @DatabaseField
    private String revokeReason;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity revokeAuthor;

    @DatabaseField
    private long revokeDate;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity playerAuthor;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity playerMuted;

}

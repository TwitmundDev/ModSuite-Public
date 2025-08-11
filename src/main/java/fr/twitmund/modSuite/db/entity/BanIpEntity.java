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
@DatabaseTable(tableName = "BanIp")
public class BanIpEntity {

    @Id
    @DatabaseField(generatedId = true)
    private int banIp_id;

    @DatabaseField
    private String ipAddress;

    @DatabaseField
    private String banReason;


    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity playerAuthor;

}

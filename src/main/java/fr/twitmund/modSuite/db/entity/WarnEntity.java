package fr.twitmund.modSuite.db.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.UUID;


@Setter
@Getter
@Entity
@DatabaseTable(tableName = "warns")
public class WarnEntity {
    @Id
    @DatabaseField(generatedId = true)
    private int warn_id;

    @DatabaseField
    private String warnReason;

    @DatabaseField
    private long warnDate;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity playerAuthor;


    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity playerWarned;

    public WarnEntity() {
    }
}

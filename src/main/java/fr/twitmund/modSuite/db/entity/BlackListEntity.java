package fr.twitmund.modSuite.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Setter
@Getter
@Entity
@DatabaseTable(tableName = "BlackListed")
public class BlackListEntity {

    @Id
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String playerName;

    @DatabaseField
    private String reason;

    @ManyToOne
    @JoinColumn(name = "player_author_id")
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    @NotNull
    private PlayerEntity playerAuthor;

    public BlackListEntity(String playerName, String reason, PlayerEntity playerAuthor) {
    }

    public BlackListEntity() {

    }
}

package fr.twitmund.modSuite.db.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.UUID;


@Setter
@Getter
@Entity
@DatabaseTable(tableName = "players")
public class PlayerEntity {
    @GeneratedValue @Id
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String player_Name;

    @DatabaseField
    @NotNull
    private String player_IP;


    public PlayerEntity() {
    }
}

package the.convenient.foodie.restaurant.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="opening_hours")
public class OpeningHours implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="mon_open")
    private LocalTime mondayOpen;
    @Column(name="mon_close")
    private LocalTime mondayClose;
    @Column(name="tue_open")
    private LocalTime tuesdayOpen;
    @Column(name="tue_close")
    private LocalTime tuesdayClose;
    @Column(name="wed_open")
    private LocalTime wednesdayOpen;
    @Column(name="wed_close")
    private LocalTime wednesdayClose;
    @Column(name="thu_open")
    private LocalTime thursdayOpen;
    @Column(name="thu_close")
    private LocalTime thursdayClose;
    @Column(name="fri_open")
    private LocalTime fridayOpen;
    @Column(name="fri_close")
    private LocalTime fridayClose;
    @Column(name="sat_open")
    private LocalTime saturdayOpen;
    @Column(name="sat_close")
    private LocalTime saturdayClose;
    @Column(name="sun_open")
    private LocalTime sundayOpen;
    @Column(name="sun_close")
    private LocalTime sundayClose;

    @Column(name="created")
    @NotNull(message = "Creation date must be specified!")
    private LocalDateTime created;

    @Column(name="created_by")
    @NotNull(message ="User that created the record must be specified!")
    private String createdBy;

    @Column(name="modified")
    private LocalDateTime modified;
    @Column(name="modified_by")
    private String modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getMondayOpen() {
        return mondayOpen;
    }

    public void setMondayOpen(LocalTime mondayOpen) {
        this.mondayOpen = mondayOpen;
    }

    public LocalTime getMondayClose() {
        return mondayClose;
    }

    public void setMondayClose(LocalTime mondayClose) {
        this.mondayClose = mondayClose;
    }

    public LocalTime getTuesdayOpen() {
        return tuesdayOpen;
    }

    public void setTuesdayOpen(LocalTime tuesdayOpen) {
        this.tuesdayOpen = tuesdayOpen;
    }

    public LocalTime getTuesdayClose() {
        return tuesdayClose;
    }

    public void setTuesdayClose(LocalTime tuesdayClose) {
        this.tuesdayClose = tuesdayClose;
    }

    public LocalTime getWednesdayOpen() {
        return wednesdayOpen;
    }

    public void setWednesdayOpen(LocalTime wednesdayOpen) {
        this.wednesdayOpen = wednesdayOpen;
    }

    public LocalTime getWednesdayClose() {
        return wednesdayClose;
    }

    public void setWednesdayClose(LocalTime wednesdayClose) {
        this.wednesdayClose = wednesdayClose;
    }

    public OpeningHours() {
    }

    public OpeningHours(Long id,LocalTime mondayOpen, LocalTime mondayClose, LocalTime tuesdayOpen, LocalTime tuesdayClose, LocalTime wednesdayOpen, LocalTime wednesdayClose, LocalTime thursdayOpen, LocalTime thursdayClose, LocalTime fridayOpen, LocalTime fridayClose, LocalTime saturdayOpen, LocalTime saturdayClose, LocalTime sundayOpen, LocalTime sundayClose, LocalDateTime created, String createdBy, LocalDateTime modified, String modifiedBy) {
        this.id=id;
        this.mondayOpen = mondayOpen;
        this.mondayClose = mondayClose;
        this.tuesdayOpen = tuesdayOpen;
        this.tuesdayClose = tuesdayClose;
        this.wednesdayOpen = wednesdayOpen;
        this.wednesdayClose = wednesdayClose;
        this.thursdayOpen = thursdayOpen;
        this.thursdayClose = thursdayClose;
        this.fridayOpen = fridayOpen;
        this.fridayClose = fridayClose;
        this.saturdayOpen = saturdayOpen;
        this.saturdayClose = saturdayClose;
        this.sundayOpen = sundayOpen;
        this.sundayClose = sundayClose;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
    }

    public OpeningHours( LocalTime mondayOpen, LocalTime mondayClose, LocalTime tuesdayOpen, LocalTime tuesdayClose, LocalTime wednesdayOpen, LocalTime wednesdayClose, LocalTime thursdayOpen, LocalTime thursdayClose, LocalTime fridayOpen, LocalTime fridayClose, LocalTime saturdayOpen, LocalTime saturdayClose, LocalTime sundayOpen, LocalTime sundayClose, LocalDateTime created, String createdBy) {
        this.mondayOpen = mondayOpen;
        this.mondayClose = mondayClose;
        this.tuesdayOpen = tuesdayOpen;
        this.tuesdayClose = tuesdayClose;
        this.wednesdayOpen = wednesdayOpen;
        this.wednesdayClose = wednesdayClose;
        this.thursdayOpen = thursdayOpen;
        this.thursdayClose = thursdayClose;
        this.fridayOpen = fridayOpen;
        this.fridayClose = fridayClose;
        this.saturdayOpen = saturdayOpen;
        this.saturdayClose = saturdayClose;
        this.sundayOpen = sundayOpen;
        this.sundayClose = sundayClose;
        this.created = created;
        this.createdBy = createdBy;
    }

    public LocalTime getThursdayOpen() {
        return thursdayOpen;
    }

    public void setThursdayOpen(LocalTime thursdayOpen) {
        this.thursdayOpen = thursdayOpen;
    }

    public LocalTime getThursdayClose() {
        return thursdayClose;
    }

    public void setThursdayClose(LocalTime thursdayClose) {
        this.thursdayClose = thursdayClose;
    }

    public LocalTime getFridayOpen() {
        return fridayOpen;
    }

    public void setFridayOpen(LocalTime fridayOpen) {
        this.fridayOpen = fridayOpen;
    }

    public LocalTime getFridayClose() {
        return fridayClose;
    }

    public void setFridayClose(LocalTime fridayClose) {
        this.fridayClose = fridayClose;
    }

    public LocalTime getSaturdayOpen() {
        return saturdayOpen;
    }

    public void setSaturdayOpen(LocalTime saturdayOpen) {
        this.saturdayOpen = saturdayOpen;
    }

    public LocalTime getSaturdayClose() {
        return saturdayClose;
    }

    public void setSaturdayClose(LocalTime saturdayClose) {
        this.saturdayClose = saturdayClose;
    }

    public LocalTime getSundayOpen() {
        return sundayOpen;
    }

    public void setSundayOpen(LocalTime sundayOpen) {
        this.sundayOpen = sundayOpen;
    }

    public LocalTime getSundayClose() {
        return sundayClose;
    }

    public void setSundayClose(LocalTime sundayClose) {
        this.sundayClose = sundayClose;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }


}

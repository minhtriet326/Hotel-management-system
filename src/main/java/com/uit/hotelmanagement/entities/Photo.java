package com.uit.hotelmanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "photos")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer photoId;

    @NotEmpty(message = "Room's name can't be empty")
    private String name;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}

package com.uit.hotelmanagement.entities;

import com.uit.hotelmanagement.utils.RoomStatus;
import com.uit.hotelmanagement.utils.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    @NotBlank(message = "Room number can't be blank")
    @Column(unique = true)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Room type can't be null")
    private RoomType roomType;

    @PositiveOrZero(message = "Room price can't be negative")
    @Column(precision = 19, scale = 10)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Room's status can't be null")
    private RoomStatus roomStatus;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Photo> photos;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "room")
    private List<BookingHistory> bookingHistories;

}

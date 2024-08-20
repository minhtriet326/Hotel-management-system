package com.uit.hotelmanagement.entities;

import com.uit.hotelmanagement.utils.HistoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "booking_histories")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookHistoryId;

    @NotNull(message = "Actual check in date can't be null")
    private LocalDate actualCheckInDate;

    @NotNull(message = "Actual check out date can't be null")
    private LocalDate actualCheckOutDate;

    @NotNull(message = "Booking history's type can't be null")
    @Enumerated(EnumType.STRING)
    private HistoryType historyType;

    private String note;

    private LocalDate changeDate; //chỗ này nên có giờ cụ thể

    @PositiveOrZero(message = "Final total price can't be negative")
    @Column(precision = 19, scale = 10)
    private BigDecimal finalTotalPrice;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "current_room_id")
    private Room room;
}

package com.uit.hotelmanagement.entities;

import com.uit.hotelmanagement.utils.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @NotNull(message = "Check in date can't be null")
    private LocalDate checkInDate;

    @NotNull(message = "Check out date can't be null")
    private LocalDate checkOutDate;

    @NotNull(message = "Room's status can't be null")
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @DecimalMin(value = "0.0")// khong dat message thu coi no bao loi gi
    @DecimalMax(value = "1.0")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal bookingVoucher;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<ServiceUsage> serviceUsages;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingHistory> bookingHistories;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    // Method
    public BigDecimal notChangeRoomCalculate() {
        long stayedDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        BigDecimal voucher = BigDecimal.ONE.subtract(bookingVoucher);

        BigDecimal roomPrice = room.getPrice();

        return roomPrice.multiply(voucher).multiply(BigDecimal.valueOf(stayedDays))
                .setScale(2, RoundingMode.HALF_EVEN);// làm tròn kiểu ngân hàng đến số chẵn gần nhất.
    }
}

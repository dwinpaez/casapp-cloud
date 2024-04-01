package ca.casapp.springcloud.msvc.bookings.core.application.persistence.entity;


import ca.casapp.springcloud.msvc.bookings.core.application.type.BookingState;
import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity for object Service
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
@Entity
@Data
@Table(name = "booking")
public class BookingEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client")
    private Long clientId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private BookingState state;

    @Column
    private Double total;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

package ca.casapp.springcloud.msvc.clients.core.aplication.repository.persistence.entity;


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
@Table(name = "client")
public class ClientEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String phone;

    @Column
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

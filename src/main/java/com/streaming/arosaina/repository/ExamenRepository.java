package com.streaming.arosaina.repository;


import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.entity.Examen;
import com.streaming.arosaina.entity.FileByModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExamenRepository extends JpaRepository<Examen,Long> {
    List<Examen> findByUser(ApplicationUser user);

    List<Examen> findByIdModule(Long idModule);

    List<Examen> findByUserAndIdModule(ApplicationUser user,Long idModule);
    //SELECT examen.user_id,examen.id_module,SUM(examen.status) FROM `examen` WHERE examen.id_module=1
    @Query("SELECT e.user,e.idModule, sum(e.status) FROM Examen e WHERE e.idModule=:userDes GROUP BY e.user")
    List<Object[]> listBExamenBySum(@Param("userDes") Long userDes);
}

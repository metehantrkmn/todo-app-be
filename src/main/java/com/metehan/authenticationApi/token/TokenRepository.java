package com.metehan.authenticationApi.token;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer> {

    //semicolon(:)variable name means declared variable which is userId parameter of method in this case
    @Query(value = """
                SELECT t FROM Token t inner join User u on t.user.id=u.id
                WHERE u.id= :userId and (t.expired = false or t.revoked = false)
            """)
    List<Token> findValidTokensById(Integer userId);

    Optional<Token> findByToken(String token);

}

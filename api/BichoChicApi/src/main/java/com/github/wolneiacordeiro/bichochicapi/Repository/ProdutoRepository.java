package com.github.wolneiacordeiro.bichochicapi.Repository;

import com.github.wolneiacordeiro.bichochicapi.entity.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProdutoRepository extends MongoRepository<Produto, String> {
}

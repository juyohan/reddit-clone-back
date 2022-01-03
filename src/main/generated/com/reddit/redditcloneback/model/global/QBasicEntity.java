package com.reddit.redditcloneback.model.global;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBasicEntity is a Querydsl query type for BasicEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBasicEntity extends EntityPathBase<BasicEntity> {

    private static final long serialVersionUID = 1286037398L;

    public static final QBasicEntity basicEntity = new QBasicEntity("basicEntity");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public QBasicEntity(String variable) {
        super(BasicEntity.class, forVariable(variable));
    }

    public QBasicEntity(Path<? extends BasicEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBasicEntity(PathMetadata metadata) {
        super(BasicEntity.class, metadata);
    }

}


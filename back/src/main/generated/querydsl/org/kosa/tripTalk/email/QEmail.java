package org.kosa.tripTalk.email;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmail is a Querydsl query type for Email
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmail extends EntityPathBase<Email> {

    private static final long serialVersionUID = -1330291233L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmail email = new QEmail("email");

    public final BooleanPath confirmed = createBoolean("confirmed");

    public final DateTimePath<java.time.LocalDateTime> expiryDate = createDateTime("expiryDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath token = createString("token");

    public final org.kosa.tripTalk.user.QUser user;

    public QEmail(String variable) {
        this(Email.class, forVariable(variable), INITS);
    }

    public QEmail(Path<? extends Email> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmail(PathMetadata metadata, PathInits inits) {
        this(Email.class, metadata, inits);
    }

    public QEmail(Class<? extends Email> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new org.kosa.tripTalk.user.QUser(forProperty("user")) : null;
    }

}


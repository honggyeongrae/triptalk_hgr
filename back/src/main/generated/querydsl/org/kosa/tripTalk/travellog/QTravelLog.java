package org.kosa.tripTalk.travellog;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTravelLog is a Querydsl query type for TravelLog
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTravelLog extends EntityPathBase<TravelLog> {

    private static final long serialVersionUID = -1835584293L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTravelLog travelLog = new QTravelLog("travelLog");

    public final org.kosa.tripTalk.category.QCategory category;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final org.kosa.tripTalk.user.QUser user;

    public QTravelLog(String variable) {
        this(TravelLog.class, forVariable(variable), INITS);
    }

    public QTravelLog(Path<? extends TravelLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTravelLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTravelLog(PathMetadata metadata, PathInits inits) {
        this(TravelLog.class, metadata, inits);
    }

    public QTravelLog(Class<? extends TravelLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new org.kosa.tripTalk.category.QCategory(forProperty("category")) : null;
        this.user = inits.isInitialized("user") ? new org.kosa.tripTalk.user.QUser(forProperty("user")) : null;
    }

}


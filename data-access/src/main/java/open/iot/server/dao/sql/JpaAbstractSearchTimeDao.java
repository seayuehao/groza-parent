package open.iot.server.dao.sql;

import com.datastax.driver.core.utils.UUIDs;
import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.page.TimePageLink;
import open.iot.server.dao.model.BaseEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public abstract class JpaAbstractSearchTimeDao<E extends BaseEntity<D>, D> extends JpaAbstractDao<E, D> {

    public static <T> Specification<T> getTimeSearchPageSpec(TimePageLink pageLink, String idColumn) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates;
                if (pageLink.isAscOrder()) {
                    predicates = createAscPredicates(pageLink, idColumn, root, criteriaBuilder);
                } else {
                    predicates = createDescPredicates(pageLink, idColumn, root, criteriaBuilder);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    private static <T> List<Predicate> createAscPredicates(TimePageLink pageLink, String idColumn, Root<T> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (pageLink.getIdOffset() != null) {
            Predicate lowerBound = criteriaBuilder.greaterThan(root.get(idColumn), UUIDConverter.fromTimeUUID(pageLink.getIdOffset()));
            predicates.add(lowerBound);
        } else if (pageLink.getStartTime() != null) {
            UUID startOf = UUIDs.startOf(pageLink.getStartTime());
            Predicate lowerBound = criteriaBuilder.greaterThanOrEqualTo(root.get(idColumn), UUIDConverter.fromTimeUUID(startOf));
            predicates.add(lowerBound);
        }
        if (pageLink.getEndTime() != null) {
            UUID endOf = UUIDs.endOf(pageLink.getEndTime());
            Predicate upperBound = criteriaBuilder.lessThanOrEqualTo(root.get(idColumn), UUIDConverter.fromTimeUUID(endOf));
            predicates.add(upperBound);
        }
        return predicates;
    }

    private static <T> List<Predicate> createDescPredicates(TimePageLink pageLink, String idColumn, Root<T> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (pageLink.getIdOffset() != null) {
            Predicate lowerBound = criteriaBuilder.lessThan(root.get(idColumn), UUIDConverter.fromTimeUUID(pageLink.getIdOffset()));
            predicates.add(lowerBound);
        } else if (pageLink.getEndTime() != null) {
            UUID endOf = UUIDs.endOf(pageLink.getEndTime());
            Predicate lowerBound = criteriaBuilder.lessThanOrEqualTo(root.get(idColumn), UUIDConverter.fromTimeUUID(endOf));
            predicates.add(lowerBound);
        }
        if (pageLink.getStartTime() != null) {
            UUID startOf = UUIDs.startOf(pageLink.getStartTime());
            Predicate upperBound = criteriaBuilder.greaterThanOrEqualTo(root.get(idColumn), UUIDConverter.fromTimeUUID(startOf));
            predicates.add(upperBound);
        }
        return predicates;
    }
}

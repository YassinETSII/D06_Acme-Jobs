
package acme.features.sponsor.commercialBanner;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.banners.CommercialBanner;
import acme.entities.customisation.Customisation;
import acme.entities.roles.Sponsor;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface SponsorCommercialBannerRepository extends AbstractRepository {

	@Query("select c from CommercialBanner c where c.id = ?1")
	CommercialBanner findOneCommercialBannerById(int id);

	@Query("select c from CommercialBanner c where c.sponsor.id = ?1")
	Collection<CommercialBanner> findManyCommercialBannersBySponsorId(int sponsorId);

	@Query("select s from Sponsor s where s.userAccount.id = ?1")
	Sponsor findOneSponsorByUserAccountId(int id);

	@Query("select c from Customisation c")
	Customisation findConfiguration();
}

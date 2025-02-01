import { ContactHelp } from '@/components/common/contact-help-section/contact-help';
import { HomeAboutUsBottom } from '@/components/home/home-about-us-bottom';
import CategoryList from '@/components/home/category-list';
import HomePage from '@/components/home/home-page';
import GetYourDreamHouse from '@/components/home/getyour-dream-house';
import ExploreProperties from '@/components/home/explore-properties';
import DiscoverPopularProperties from '@/components/home/discover-popular-properties';
import Spacer from '@/components/common/Spacer';
import { getAdvertTypes } from '@/services/advert-type-service';
import { getCitiesForHomePage } from '@/services/address-service';
import { getCategoriesForHomePage } from '@/services/category-service';
import {auth} from '@/auth';

const Page = async () => {
  const session = await auth();
  const [cities,  categories] = await Promise.all([
    getCitiesForHomePage(),
    getCategoriesForHomePage()
  ]);

  const advertTypes = await getAdvertTypes();
  return (
    <>
      <HomePage types={advertTypes?.object} />
      <DiscoverPopularProperties />
      <CategoryList category={categories.object} />
      <Spacer height={60} />
      <ExploreProperties cities={cities.object} />
      <GetYourDreamHouse session={session} />

      <HomeAboutUsBottom />
      <ContactHelp />
    </>
  );
};

export default Page;

import { Spacer } from "@/components/common/spacer/spacer";
import MyAdverts from "@/components/dashboard/my-adverts/my-adverts";
import { brand } from "@/constants/brand";
import UserPageHeader from "@/components/common/UserPageHeader";
import { getAdvertAll } from "@/services/advert-service";
import { wait } from "@/utils/wait";
import ErrorPage from "@/errors/ErrorPage";

export const metadata = {
  title: {
    default: brand.title,
    template: `${brand.name} | My Adverts`
  }
};



const MyAdvertsPage = async (props) => {
  const searchParams = await props.searchParams;

  const currentPage = Math.max(parseInt(searchParams.page) || 1, 1);  
  const size = searchParams.size || 10;
  const sort = searchParams.sort || 'createdAt';
  const type = searchParams.type || 'asc';
  const searchTerm = searchParams.q || '';
  
    try{
      const res = await getAdvertAll(currentPage-1, size, sort, type, searchTerm);
      return (
    <>
      <UserPageHeader text="My Adverts" />
      <MyAdverts data={res} />
      <Spacer />
    </>
  );
    }catch(err){
      return (
    <>
      <ErrorPage message={err} /> 
    </>
  );
    }
  
};

export default MyAdvertsPage;
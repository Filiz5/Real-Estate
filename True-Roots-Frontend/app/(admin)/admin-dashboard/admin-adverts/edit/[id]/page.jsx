import AdminAdvertEdit from "@/components/admin/admin-adverts-edit/AdminAdvertEdit";
import { getAdvertByIdAdmin } from "@/services/advert-service";
import { getUserByAdvertId } from "@/services/user-service";
import { wait } from "@/utils/wait";

export default async function UpdateAdvert(props) {

  const { id } = await props.params;

  const [advertResponse, userResponse] = await Promise.all([
    getAdvertByIdAdmin(id),
    getUserByAdvertId(id)
  ]);
  await wait(2000);
  return (
    <>
    <AdminAdvertEdit 
      advert={advertResponse} 
      user={userResponse}
     />
    </>
  )
}

"use client";

import { AdminUserEdit } from "@/components/admin/user/AdminUserEdit"; // Named import

  const Page = ({ params }) => {
  const { id } = params;

  return <AdminUserEdit userId={id} />;
 
};

export default Page;
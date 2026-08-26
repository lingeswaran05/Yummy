import {
  LayoutDashboard,
  Heart,
  ShoppingCart,
  CirclePlus,
  LogOut,
  ChefHat,
} from "lucide-react";

import { NavLink, useNavigate } from "react-router-dom";

function Sidebar() {
  const navigate = useNavigate();

  const navItems = [
    {
      name: "Dashboard",
      path: "/dashboard",
      icon: LayoutDashboard,
    },
    {
      name: "Wishlist",
      path: "/wishlist",
      icon: Heart,
    },
    {
      name: "Shopping List",
      path: "/shopping-list",
      icon: ShoppingCart,
    },
    {
      name: "Add Recipe",
      path: "/add-recipe",
      icon: CirclePlus,
    },
  ];

  const handleLogout = () => {
    navigate("/");
  };

  return (
    <aside className="hidden h-screen w-64 shrink-0 flex-col justify-between border-r border-[#e4e2e1] bg-white md:flex lg:w-72">

      {/* Top */}
      <div className="flex flex-col gap-8 overflow-y-auto p-6">

        {/* Logo */}
        <button
          type="button"
          onClick={() => navigate("/dashboard")}
          className="flex items-center gap-2 text-left"
        >
          <ChefHat className="h-7 w-7 text-primary" />

          <span className="text-xl font-bold text-primary">
            Yummiee
          </span>
        </button>

        {/* Navigation */}
        <nav className="flex flex-col gap-2">

          {navItems.map((item) => {
            const Icon = item.icon;

            return (
              <NavLink
                key={item.path}
                to={item.path}
                className={({ isActive }) =>
                  `flex items-center gap-3 rounded-xl p-3 transition-colors ${
                    isActive
                      ? "bg-primary/10 font-bold text-primary"
                      : "text-text-secondary hover:bg-[#f6f3f2] hover:text-primary"
                  }`
                }
              >
                <Icon className="h-5 w-5" />

                <span>{item.name}</span>
              </NavLink>
            );
          })}

        </nav>
      </div>

      {/* Logout */}
      <div className="border-t border-[#e4e2e1] p-6">

        <button
          type="button"
          onClick={handleLogout}
          className="flex w-full items-center gap-3 rounded-xl p-3 text-left text-text-secondary transition-colors hover:bg-[#f6f3f2] hover:text-primary"
        >
          <LogOut className="h-5 w-5" />

          <span>Log out</span>
        </button>

      </div>

    </aside>
  );
}

export default Sidebar;
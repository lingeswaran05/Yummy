import { useState } from "react";
import {
  ChefHat,
  Eye,
  EyeOff,
  LockKeyhole,
  Mail,
} from "lucide-react";

import { Link, useNavigate } from "react-router-dom";

function Login() {
  const navigate = useNavigate();

  const [showPassword, setShowPassword] =
    useState(false);

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");

  const handleChange = (event) => {
    setFormData((current) => ({
      ...current,
      [event.target.name]: event.target.value,
    }));

    setError("");
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!formData.email || !formData.password) {
      setError("Please enter your email and password.");
      return;
    }

    // Temporary frontend login.
    // Backend authentication will replace this later.

    localStorage.setItem(
      "yummiee-user",
      JSON.stringify({
        email: formData.email,
      })
    );

    navigate("/dashboard");
  };

  return (
    <main className="min-h-screen bg-[#faf8f7] px-5 py-10">

      <div className="mx-auto flex min-h-[calc(100vh-80px)] w-full max-w-[560px] items-center justify-center">

        <div className="w-full rounded-[24px] bg-white p-7 shadow-[0_8px_30px_rgba(0,0,0,0.06)] sm:p-10">

          {/* Brand */}
          <div className="text-center">

            <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-primary/10">
              <ChefHat className="h-6 w-6 text-primary" />
            </div>

            <h1 className="mt-4 text-3xl font-bold text-primary">
              Yummiee
            </h1>

            <p className="mt-2 text-sm text-text-secondary">
              Your recipes. Your shopping list. One place.
            </p>

          </div>

          {/* Heading */}
          <div className="mt-10">

            <h2 className="text-2xl font-bold">
              Welcome back!
            </h2>

            <p className="mt-2 text-text-secondary">
              Sign in to continue to Yummiee.
            </p>

          </div>

          {/* Form */}
          <form
            onSubmit={handleSubmit}
            className="mt-8 space-y-5"
          >

            {/* Email */}
            <div>

              <label className="mb-2 block text-sm font-semibold">
                Email
              </label>

              <div className="relative">

                <Mail className="absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-text-secondary" />

                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="Enter your email"
                  className="h-14 w-full rounded-xl border border-border bg-white pl-12 pr-4 outline-none transition focus:border-primary focus:ring-4 focus:ring-primary/10"
                />

              </div>

            </div>

            {/* Password */}
            <div>

              <label className="mb-2 block text-sm font-semibold">
                Password
              </label>

              <div className="relative">

                <LockKeyhole className="absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-text-secondary" />

                <input
                  type={
                    showPassword
                      ? "text"
                      : "password"
                  }
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  placeholder="Enter your password"
                  className="h-14 w-full rounded-xl border border-border bg-white pl-12 pr-12 outline-none transition focus:border-primary focus:ring-4 focus:ring-primary/10"
                />

                <button
                  type="button"
                  onClick={() =>
                    setShowPassword(
                      (current) => !current
                    )
                  }
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-text-secondary hover:text-primary"
                >
                  {showPassword ? (
                    <EyeOff className="h-5 w-5" />
                  ) : (
                    <Eye className="h-5 w-5" />
                  )}
                </button>

              </div>

            </div>

            {/* Error */}
            {error && (
              <div className="rounded-xl bg-red-50 px-4 py-3 text-sm font-medium text-red-600">
                {error}
              </div>
            )}

            {/* Options */}
            <div className="flex items-center justify-between text-sm">

              <label className="flex cursor-pointer items-center gap-2 text-text-secondary">

                <input
                  type="checkbox"
                  className="h-4 w-4 rounded border-border accent-primary"
                />

                Remember me

              </label>
<Link
  to="/forgot-password"
  className="font-semibold text-primary hover:underline"
>
  Forgot password?
</Link>

            </div>

            {/* Login */}
            <button
              type="submit"
              className="h-14 w-full rounded-xl bg-primary font-bold text-white shadow-[0_4px_12px_rgba(174,49,21,0.2)] transition hover:bg-primary-dark"
            >
              Login
            </button>

          </form>

          {/* Register */}
          <p className="mt-8 text-center text-sm text-text-secondary">

            Don't have an account?{" "}

            <Link
              to="/register"
              className="font-bold text-primary hover:underline"
            >
              Create an account
            </Link>

          </p>

        </div>

      </div>

    </main>
  );
}

export default Login;
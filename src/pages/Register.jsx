import { useState } from "react";
import {
  ChefHat,
  Eye,
  EyeOff,
  LockKeyhole,
  Mail,
  User,
} from "lucide-react";

import {
  Link,
  useNavigate,
} from "react-router-dom";

function Register() {
  const navigate = useNavigate();

  const [showPassword, setShowPassword] =
    useState(false);

  const [showConfirmPassword, setShowConfirmPassword] =
    useState(false);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
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

    if (
      !formData.name ||
      !formData.email ||
      !formData.password ||
      !formData.confirmPassword
    ) {
      setError("Please fill in all fields.");
      return;
    }

    if (formData.password.length < 6) {
      setError(
        "Password must contain at least 6 characters."
      );
      return;
    }

    if (
      formData.password !==
      formData.confirmPassword
    ) {
      setError("Passwords do not match.");
      return;
    }

    // Temporary frontend registration.
    // Backend registration will replace this later.

    localStorage.setItem(
      "yummiee-user",
      JSON.stringify({
        name: formData.name,
        email: formData.email,
      })
    );

    navigate("/dashboard");
  };

  return (
    <main className="min-h-screen bg-[#faf8f7] px-5 py-10">

      <div className="mx-auto w-full max-w-[620px]">

        {/* Brand */}
        <div className="mb-8 flex items-center gap-2">

          <ChefHat className="h-7 w-7 text-primary" />

          <span className="text-xl font-bold text-primary">
            Yummiee
          </span>

        </div>

        {/* Card */}
        <div className="rounded-[24px] bg-white p-7 shadow-[0_8px_30px_rgba(0,0,0,0.06)] sm:p-10">

          <h1 className="text-3xl font-bold">
            Create your Yummiee account
          </h1>

          <p className="mt-2 text-text-secondary">
            Start organizing your recipes and shopping smarter.
          </p>

          <form
            onSubmit={handleSubmit}
            className="mt-8 space-y-5"
          >

            {/* Name */}
            <div>

              <label className="mb-2 block text-sm font-semibold">
                Full Name
              </label>

              <div className="relative">

                <User className="absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-text-secondary" />

                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  placeholder="e.g. Jane Doe"
                  className="h-14 w-full rounded-xl border border-border pl-12 pr-4 outline-none transition focus:border-primary focus:ring-4 focus:ring-primary/10"
                />

              </div>

            </div>

            {/* Email */}
            <div>

              <label className="mb-2 block text-sm font-semibold">
                Email Address
              </label>

              <div className="relative">

                <Mail className="absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-text-secondary" />

                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="you@example.com"
                  className="h-14 w-full rounded-xl border border-border pl-12 pr-4 outline-none transition focus:border-primary focus:ring-4 focus:ring-primary/10"
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
                  placeholder="Create a password"
                  className="h-14 w-full rounded-xl border border-border pl-12 pr-12 outline-none transition focus:border-primary focus:ring-4 focus:ring-primary/10"
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

            {/* Confirm Password */}
            <div>

              <label className="mb-2 block text-sm font-semibold">
                Confirm Password
              </label>

              <div className="relative">

                <LockKeyhole className="absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-text-secondary" />

                <input
                  type={
                    showConfirmPassword
                      ? "text"
                      : "password"
                  }
                  name="confirmPassword"
                  value={
                    formData.confirmPassword
                  }
                  onChange={handleChange}
                  placeholder="Confirm your password"
                  className="h-14 w-full rounded-xl border border-border pl-12 pr-12 outline-none transition focus:border-primary focus:ring-4 focus:ring-primary/10"
                />

                <button
                  type="button"
                  onClick={() =>
                    setShowConfirmPassword(
                      (current) => !current
                    )
                  }
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-text-secondary hover:text-primary"
                >
                  {showConfirmPassword ? (
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

            {/* Submit */}
            <button
              type="submit"
              className="h-14 w-full rounded-xl bg-primary font-bold text-white shadow-[0_4px_12px_rgba(174,49,21,0.2)] transition hover:bg-primary-dark"
            >
              Create Account
            </button>

          </form>

          {/* Login */}
          <p className="mt-8 text-center text-sm text-text-secondary">

            Already have an account?{" "}

            <Link
              to="/"
              className="font-bold text-primary hover:underline"
            >
              Login
            </Link>

          </p>

        </div>

      </div>

    </main>
  );
}

export default Register;
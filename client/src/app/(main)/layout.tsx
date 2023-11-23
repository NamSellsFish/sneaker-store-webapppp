import NavBar from "./_components/NavBar";
import Footer from "./_components/Footer";

export default function RootLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <main>
            <NavBar />
            {children}
            <Footer />
        </main>
    );
}

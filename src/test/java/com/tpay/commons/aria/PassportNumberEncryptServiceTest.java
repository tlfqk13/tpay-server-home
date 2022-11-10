package com.tpay.commons.aria;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PassportNumberEncryptServiceTest {

    PassportNumberEncryptService encryptService = new PassportNumberEncryptService();
    PassportNumberDecryptService decryptService = new PassportNumberDecryptService();

    @Test
    void passportEncryptionTest() {
        String passportNumber = "SUCCESS15";
        String encryptPassportNumber = encryptService.encrypt(passportNumber);
        System.out.println("encryptPassportNumber = " + encryptPassportNumber);
        String decryptPassportNumber = decryptService.decrypt(encryptPassportNumber);
        System.out.println("decryptPassportNumber = " + decryptPassportNumber);
        assertThat(passportNumber).isEqualTo(decryptPassportNumber);
    }

    @Test
    void passportDecryptionTest() {
        String encryptPassportNumber = "AE0B6EA1894FFC38694AE3B22056A8DF";
        String decryptPassportNumber = decryptService.decrypt(encryptPassportNumber);
        System.out.println("decryptPassportNumber = " + decryptPassportNumber);
    }

    @Test
    void passportDecrp(){
        String[] arr = new String[] {
                "B7242243FEF3C4744B9A828CFBEB85A7",
                "D828A7AB9930A113FE36CC4E2D52532E",
                "F2B93337730C31BF43BFE048FD39A81D",
                "06AADC347B356ECE25E9796D3F9E7A1B",
                "39088DB6D25E417BAE205A06807DF7D7",
                "DA734E71C7B4D9FA2D4A333F4626BE0E",
                "34F88CCCB3BCE4014CFF56764447DEFD",
                "0A6432A1017BCEFA91892D579F876690",
                "ADB29F440F0B4E8C6503C82F097233BD",
                "28D38D4985170887F8112AFBE36D67DD",
                "FA2F9D136DEAD4EE195702A72F6C62BA",
                "CC6AE95DD208DE9F5A64889349E53ED0",
                "FEF83A03F03081B34DA4E68763D1BE51",
                "A885B765F20177440A83E266563D2E45",
                "7EE500C570EBE7C25E84E0E2AEF78D6E",
                "029FE9444E71D223E6C725578315B274",
                "FF6962359B371F3FF63881DFE87D7382",
                "9360C4B79404AE2981A8C4F559477268",
                "8C12911B61480D3316E392CEA946205C",
                "284F01F553BFCA8F9987A1427FFD403A",
                "FC9D4B4053631130F309F5C0576F55DE",
                "FC5E3E04D4224F4E1F3C19AA8AB43377",
                "4556CA63350CD764707B4B58AE40D372",
                "A55AD9DABFFE1D560F76C4F43BDDA79D",
                "49620F09493C00D6B8E0481B8ABC7380",
                "38EAD8D5FDB9916B7B381E3606997735",
                "550868B40B10DA75B3F56AFF74A7E112",
                "4A9A739C81906A536C4ABEC2A1C8EE64",
                "4A50AAF556ABE8E84F3E560B21B81F35",
                "7F3373E127EADB2A9871D1E148D64F82",
                "1FAA50EFD0CC06E635DCFEB9FE2CE800",
                "B8B0C85DB29F43DB790487229AC79043",
                "7CA285248630EF260EE81ABAE61ECC76",
                "388252013E406B528C1D9F05C1A4B514",
                "B52DE041199A25B8F9D7AADA60740B9D",
                "7A04023C27B866C78B5EA3845AC8CB1A",
                "06D347F6EF0ED5E9053BD49CDC39A1E8",
                "778A249B5568D41B87CBB8D9945B39A5",
                "AD0BEB9781C42EA130C6555869E14CFB",
                "DEF3D430DAF8C1B01FE23C87C7B23F59",
                "D6824A9718FF6C23341C59660D8611AD",
                "A4A6B1ABFD20A08BB25DB3A13113AD1E",
                "27916C914F3EC7BA1CE8F174579A0522",
                "B9BE0CDC379660503298E8B51CA4A808",
                "065EE28D4F06446EF54C0A46D61CBB9A",
                "3BBB0B69BFB01FB24199385CA0CF6E0D",
                "CAC7AD9D49049B44E9D58DB3E2DCE001",
                "54B9B75D24793730B5B170E0ACF5AB15",
                "E80CD16F0C889C565FAB6A950154438C",
                "88018A5E40F167677667CD0841677ABE",
                "E1681BFC111A93F3A550179A61BA7A90",
                "D9210C6FEDFB1804A923165F6B9C599E",
                "D9FCE89C74F0DA684A6065E70F4A79F4",
                "568CED9C6DDC4B4BCAD72D22BF84042C",
                "488D573D2F660F886359180C6AF36739",
                "338440751DD307A0464332A4DF39FA0E",
                "90E3CB91D79E8D2EEE8ED77285112AA2",
                "576F6428C1D9BA23C875BFC7102DC154",
                "336B6EE091642E49D32633EA6DD54E20",
                "7F8C77EE54D55B1DC209F8D3B1D8DCCF",
                "BADF45D72966049D1227850323F636E1",
                "9940C9842C141A8390267E633CE73A84",
                "862B0223130DC48AF146C3F1C148B412",
                "6CF976BC6824D9A033F3A3ABDF174DC9",
                "D9FA4F822463E367255458B12AAE3FFD",
                "91292BE6FD170BDF3395970CCA0BBDF9",
                "B3D43486777E7E0D879F1E4667BE6AE1",
                "AD49F3EA71A5F2B1D2B03B81860403D1"
        };
        for (String s : arr) {
            String decryptPassportNumber = decryptService.decrypt(s);
            System.out.println("decryptPassportNumber = " + decryptPassportNumber);
        }
    }
}
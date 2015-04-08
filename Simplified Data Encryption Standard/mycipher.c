/*
 * Adam Merille
 * Variation on Simplified Data Encryption Standard algorithm
 * Should not be used for real encryption.
 *
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <unistd.h>

unsigned char key1[8];
unsigned char key2[8];

void keyGen(unsigned char inputkey[])
{
	unsigned char p10[] = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
	unsigned char p8[] = {6, 3, 7, 4, 8, 5, 10, 9};
	unsigned char* perm10 = calloc(10, sizeof(char));
	unsigned char L5[5];
	unsigned char R5[5];
	int i;

	for(i = 0; i < 10; i++)									//loop through for first permutation
	{
		perm10[i] = inputkey[(p10[i] - 1)];
	}	

															//circular left shift first 5 bits
	L5[0] = perm10[1];
	L5[1] = perm10[2];
	L5[2] = perm10[3];
	L5[3] = perm10[4];
	L5[4] = perm10[0];

															//circular left shift second 5 bits
	R5[0] = perm10[6];
	R5[1] = perm10[7];
	R5[2] = perm10[8];
	R5[3] = perm10[9];
	R5[4] = perm10[5];

															//loop through applying p8 get key1
	for(i = 0; i < 8; i++)
	{
		if(p8[i] <= 5)
		{
			key1[i] = L5[(p8[i] - 1)];
		}
		else
		{	
			key1[i] = R5[(p8[i] - 5 - 1)];					// -5 to split array
		}
	}

															//circular left shift first 5 bits twice from before P8
	unsigned char tmp = L5[0];
	L5[0] = L5[1];
	L5[1] = L5[2];
	L5[2] = L5[3];
	L5[3] = L5[4];
	L5[4] = tmp;

	tmp = L5[0];
	L5[0] = L5[1];
	L5[1] = L5[2];
	L5[2] = L5[3];
	L5[3] = L5[4];
	L5[4] = tmp;

															//circular left shift second 5 bits twice from before P8
	tmp = R5[0];
	R5[0] = R5[1];
	R5[1] = R5[2];
	R5[2] = R5[3];
	R5[3] = R5[4];
	R5[4] = tmp;

	tmp = R5[0];
	R5[0] = R5[1];
	R5[1] = R5[2];
	R5[2] = R5[3];
	R5[3] = R5[4];
	R5[4] = tmp;

															//loop through applying p8 get key2
	for(i = 0; i < 8; i++)
	{
		if(p8[i] <= 5)
		{
			key2[i] = L5[(p8[i] - 1)];
		}
		else
		{	
			key2[i] = R5[(p8[i] - 5 - 1)];					// -5 to split array
		}
	}
	free(perm10);
}

unsigned char* initPerm(unsigned char in[])
{
	unsigned char ip[] = {2, 6, 3, 1, 4, 8, 5, 7};			//Initial Permutation
	unsigned char *out = malloc(8 * sizeof(char));
	int i;
	for(i = 0; i < 8; i ++)
	{
		out[i] = in[(ip[i] - 1)];
	}

	return out;												//return permuted bits
}

unsigned char* finalPerm(unsigned char in[])
{
	unsigned char ipinv[] = {4, 1, 3, 5, 7, 2, 8, 6};		//Final Permutation
	unsigned char *out = malloc(8 * sizeof(char));
	int i;
	for(i = 0; i < 8; i ++)
	{
		out[i] = in[(ipinv[i] - 1)];
	}

	return out;												//return permuted bits
}

unsigned char* functionF(unsigned char* in, unsigned char* key)
{
	unsigned char ep[] = {4, 1, 2, 3, 2, 3, 4, 1};
	unsigned char p4[] = {2, 4, 3, 1};
	unsigned char* tmp4 = malloc(4 * sizeof(char));			//array for holding bits computed by using s-boxes
	unsigned char* ptmp4 = malloc(4 * sizeof(char));		//array for holding permuted s-box results
	unsigned char* tmp8 = malloc(4 * sizeof(char));			//array for expanding and manipulating first 4 bits
	unsigned char s0[][4] = {{1, 0, 3, 2}, 					//s-box matrices for generating 2-bit components
							{3, 2, 1, 0}, 
							{0, 2, 1, 3}, 
							{3, 1, 3, 2}};

	unsigned char s1[][4] = {{0, 1, 2, 3}, 
							{2, 0, 1, 3}, 
							{3, 0, 1, 0}, 
							{2, 1, 0, 3}};

	unsigned char* expand = malloc(8 * sizeof(char));

	int i;
	for(i = 0; i < 8; i++)								//expand and permute right 4 bits
	{
		expand[i] = in[(ep[i] + 4 - 1)];					// +4 because get from right in
	}

	for(i = 0; i < 8; i++)								// XOR expanded with key
	{
		tmp8[i] = expand[i] ^ key[i];
	}

														//S-box matrix operations, add 1st and 4th bits to get row, 
														//then 2nd and 3rd to get col, convert value to two bits
														//repeat for second set of 4 bits
	unsigned char first = s0[((tmp8[0] << 1) + tmp8[3])][((tmp8[1] << 1) + tmp8[2])];
	unsigned char second = s1[((tmp8[4] << 1) + tmp8[7])][((tmp8[5] << 1) + tmp8[6])];

	tmp4[0] = (first & 0x02) >> 1;
	tmp4[1] = (first & 0x01);
	tmp4[2] = (second & 0x02) >> 1;
	tmp4[3] = (second & 0x01);
	
														//4 bit permutation
	for(i = 0; i < 4; i++)
	{
		ptmp4[i] = tmp4[(p4[i] - 1)];
	}

														//xor ptmp4 with left of input
	for (i = 0; i < 4; i++)
	{
		in[i] = in[i] ^ ptmp4[i];
	}
	
	free(tmp4);
	free(ptmp4);
	free(tmp8);
	free(expand);
	return in;
}

unsigned char* sw(unsigned char* in)
{
	unsigned char tmp[4];
	int i;
	for(i = 0; i < 4; i++)								//store first 4 bits, so don't overwrite
	{
		tmp[i] = in[i];
	}

	for(i = 0; i < 8; i++)								//walk through swapping
	{
		if(i < 4)
		{
			in[i] = in[(i + 4)];
		}
		else
		{
			in[i] = tmp[(i - 4)];
		}
	}

	return in;
}

void encrypt(FILE *in, FILE *out, unsigned char* init_vector)
{
	unsigned char* text = calloc(1, sizeof(char));		//holder for bytes encrypted
	unsigned char* bitarr = malloc(8 * sizeof(char));	//array for bit manipulation

														//read file in binary
	while((fread(text, 1, 1, in)) > 0)	
	{
		int i;
														//grab bits and shift, store in array 
		bitarr[0] = (*text & 0x80) >> 7;
		bitarr[1] = (*text & 0x40) >> 6;
		bitarr[2] = (*text & 0x20) >> 5;
		bitarr[3] = (*text & 0x10) >> 4;
		bitarr[4] = (*text & 0x08) >> 3;
		bitarr[5] = (*text & 0x04) >> 2;
		bitarr[6] = (*text & 0x02) >> 1;
		bitarr[7] = (*text & 0x01);
		
														//cipher block chaining
		for(i = 0; i < 8; i++)
		{
			bitarr[i] = bitarr[i] ^ init_vector[i];
		}

														//function with key1
														//swap output of function with key 1
														//function with key2
		bitarr = finalPerm(functionF(sw(functionF(initPerm(bitarr), key1)), key2));
		bzero(text, 1);

														//write cipher as new vector
		for(i = 0; i < 8; i++)
		{
			init_vector[i] = bitarr[i];
		}

														//convert bits to byte
		*text = *text + (bitarr[0] << 7);
		*text = *text + (bitarr[1] << 6);
		*text = *text + (bitarr[2] << 5);
		*text = *text + (bitarr[3] << 4);
		*text = *text + (bitarr[4] << 3);
		*text = *text + (bitarr[5] << 2);
		*text = *text + (bitarr[6] << 1);
		*text = *text + (bitarr[7]);

														//write file in binary
		fwrite(text, 1, 1, out);
		bzero(text, 1);
	}
	free(bitarr);
	free(text);
}

void decrypt(FILE *in, FILE *out, unsigned char* init_vector)
{
	unsigned char* text = calloc(1, sizeof(char));			//holder for bytes to decrypt
	unsigned char* bitarr = malloc(8 * sizeof(char));		//bit manipulation array
	unsigned char* next_vector = calloc(8, sizeof(char));	//vector to be used on next decryption
															//read file in binary
	while((fread(text, 1, 1, in)) > 0)
	{
		int i;
															//grab bits and shift, store in array 
		bitarr[0] = (*text & 0x80) >> 7;
		bitarr[1] = (*text & 0x40) >> 6;
		bitarr[2] = (*text & 0x20) >> 5;
		bitarr[3] = (*text & 0x10) >> 4;
		bitarr[4] = (*text & 0x08) >> 3;
		bitarr[5] = (*text & 0x04) >> 2;
		bitarr[6] = (*text & 0x02) >> 1;
		bitarr[7] = (*text & 0x01);

															//write cipher as next vector
		for(i = 0; i < 8; i++)
		{
			next_vector[i] = bitarr[i];
		}

															//function with key2
															//swap output of function with key 2
															//function with key1
		bitarr = finalPerm(functionF(sw(functionF(initPerm(bitarr), key2)), key1));
		bzero(text, 1);

															//Cipher block chaining
		for(i = 0; i < 8; i++)
		{
			bitarr[i] = bitarr[i] ^ init_vector[i];
			init_vector[i] = next_vector[i];
		}


															//convert bits to char
		*text = *text + (bitarr[0] << 7);
		*text = *text + (bitarr[1] << 6);
		*text = *text + (bitarr[2] << 5);
		*text = *text + (bitarr[3] << 4);
		*text = *text + (bitarr[4] << 3);
		*text = *text + (bitarr[5] << 2);
		*text = *text + (bitarr[6] << 1);
		*text = *text + (bitarr[7]);

															//write file in binary
		fwrite(text, 1, 1, out);
		bzero(text, 1);
	}
	free(bitarr);
	free(text);
}
 
int main(int argc, char *argv[])
{
	char* infilename;
	char* outfilename;
	unsigned char* init_vector = malloc(8 * sizeof(char));
	unsigned char* init_key = malloc(10 * sizeof(char));
	char* vectorstr;
	char* keystr;
	int decryp = 0;
	//check inputs
	if(argc < 5 || argc > 6)					
	{
		printf("Usage: mycipher [-d] <init_key> <init_vector> <original_file> <result_file>\n");
		return 2;
	}
	if(argc == 5)
	{
		if(strncmp(argv[1], "-d", 2) == 0)
		{
			printf("Usage: mycipher [-d] <init_key> <init_vector> <original_file> <result_file>\n");
			return 3;
		}
	}

	//check for -d decryption
	if(argc == 6)				
	{
		if(strncmp(argv[1], "-d", 2) != 0)
		{
			printf("Usage: mycipher [-d] <init_key> <init_vector> <original_file> <result_file>\n");
			return 3;
		}
		decryp = 1;
		keystr = argv[2];
		vectorstr = argv[3];
		infilename = argv[4];
		outfilename = argv[5];
	}

	else
	{
		keystr = argv[1];
		vectorstr = argv[2];
		infilename = argv[3];
		outfilename = argv[4];
	}

 	//convert key to bit array and check if 0 or 1
 	if(strlen(keystr) == 10)
 	{
	 	init_key[0] = keystr[0] - 0x30;// convert from ascii number to int number
		init_key[1] = keystr[1] - 0x30;
		init_key[2] = keystr[2] - 0x30;
		init_key[3] = keystr[3] - 0x30;
		init_key[4] = keystr[4] - 0x30;
		init_key[5] = keystr[5] - 0x30;
		init_key[6] = keystr[6] - 0x30;
		init_key[7] = keystr[7] - 0x30;
		init_key[8] = keystr[8] - 0x30;
		init_key[9] = keystr[9] - 0x30;
	}

	else
	{
		printf("Initial key needs to be 10 bits only!\n");
		return 6;
	}

	int i;
	for(i = 0; i < 10; i++)
	{
		if((init_key[i] > 1 ))
		{
			printf("Initial key needs to be 0s or 1s only, as in binary\n");
			return 7;
		}
	}

	//convert vector to bit array and check if 0 or 1
	if(strlen(vectorstr) == 8)
	{
	 	init_vector[0] = vectorstr[0] - 0x30;
		init_vector[1] = vectorstr[1] - 0x30;
		init_vector[2] = vectorstr[2] - 0x30;
		init_vector[3] = vectorstr[3] - 0x30;
		init_vector[4] = vectorstr[4] - 0x30;
		init_vector[5] = vectorstr[5] - 0x30;
		init_vector[6] = vectorstr[6] - 0x30;
		init_vector[7] = vectorstr[7] - 0x30;
	}
	else
	{
		printf("Initial vector needs to be 8 bits only!\n");
		return 4;
	}

	for(i = 0; i < 8; i++)
	{
		if(init_vector[i] > 1)
		{
			printf("Initial vector needs to be 0s or 1s only, as in binary\n");
			return 5;
		}
	}


	FILE *in = fopen(infilename, "rb");
												//Error checking if file not found
	if(in == NULL)
	{
		perror("Could not open original_file!\n");
		exit(-1);
	}

	FILE *out = fopen(outfilename, "wb+");
												//Error check if cannot create file
	if(out == NULL)
	{
		perror("Could not create result_file!\n");
		exit(-1);	
	}

	keyGen(init_key);							//generate keys for algorithms

	if(!decryp)
	{
		encrypt(in, out, init_vector);
		printf("Encrypted %s as %s\n", infilename, outfilename);
	}

	else
	{
		decrypt(in, out, init_vector);
		printf("Dencrypted %s as %s\n", infilename, outfilename);
	}

	fclose(in);
	fclose(out);

	return 1;
}
